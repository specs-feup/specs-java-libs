#!/usr/bin/env node
// Usage: node convert-xstream-config.mjs INPUT.xml OUTPUT.json
// Requires Node.js 22+. Preserves INPUT and refuses to overwrite OUTPUT.
import { readFile, writeFile } from 'node:fs/promises';
import { resolve } from 'node:path';
import { pathToFileURL } from 'node:url';

const store = 'org.suikasoft.jOptions.DataStore.SimpleDataStore';
const strings = 'pt.up.fe.specs.util.utilities.StringList';
const gui = 'pt.up.fe.specs.guihelper.BaseTypes.';
const aliases = {
  SimpleDataStore: store, SetupData: `${gui}SetupData`, FieldValue: `${gui}FieldValue`,
  ListOfSetups: `${gui}ListOfSetups`, RawType: `${gui}RawType`, StringList: strings, JsonStringList: strings,
  FileList: 'org.suikasoft.jOptions.Options.FileList',
  OptionalFile: 'org.lara.interpreter.joptions.keys.OptionalFile',
  object: 'java.lang.Object', string: 'java.lang.String', int: 'java.lang.Integer', boolean: 'java.lang.Boolean',
  long: 'java.lang.Long', short: 'java.lang.Short', byte: 'java.lang.Byte',
  double: 'java.lang.Double', float: 'java.lang.Float', char: 'java.lang.Character',
  file: 'java.io.File', optional: 'java.util.Optional', map: 'java.util.HashMap',
  list: 'java.util.ArrayList', set: 'java.util.HashSet',
  'linked-hash-map': 'java.util.LinkedHashMap', 'linked-list': 'java.util.LinkedList',
  'linked-hash-set': 'java.util.LinkedHashSet', 'tree-map': 'java.util.TreeMap', 'tree-set': 'java.util.TreeSet',
  'big-int': 'java.math.BigInteger', 'big-decimal': 'java.math.BigDecimal',
};
// Field declarations determine where Jackson requires a polymorphic type wrapper.
const schemas = {
  [store]: { name: 'string', values: 'map', strict: 'boolean' },
  [`${gui}SetupData`]: { dataset: 'map', setupName: 'string', setupFile: 'file' },
  [`${gui}FieldValue`]: { rawValue: 'object', type: 'pt.up.fe.specs.guihelper.FieldType' },
  [`${gui}ListOfSetups`]: { setupList: 'list', mapOfSetups: 'map', preferredIndex: 'int' },
  'org.suikasoft.jOptions.Options.FileList': { data: store },
  'org.lara.interpreter.joptions.keys.FileList': { fileList: 'list' },
  'org.lara.interpreter.joptions.keys.OptionalFile': { file: 'file', used: 'boolean' },
};
const enums = {
  'pt.up.fe.specs.clava.language.Standard': new Set('C89 C90 C99 C11 C17 C18 C23 GNU90 GNU99 GNU11 GNU17 GNU18 GNU23 CXX98 CXX03 CXX11 CXX14 CXX17 CXX20 CXX23 CXX26 GNUXX98 GNUXX11 GNUXX14 GNUXX17 GNUXX20 GNUXX23 GNUXX26 OPENCL10 OPENCL12 OPENCL20 OPENCLCXX CUDA'.split(' ')),
  'pt.up.fe.specs.clang.LibcMode': new Set('AUTO BUILTIN_AND_LIBC SYSTEM'.split(' ')),
  'pt.up.fe.specs.guihelper.FieldType': new Set('string folder bool integer doublefloat stringList multipleChoice multipleChoiceSetup multipleChoiceStringList setup integratedSetup setupList'.split(' ')),
  'pt.up.fe.specs.guihelper.BaseTypes.RawType': new Set('String ListOfStrings Setup MapOfSetups'.split(' ')),
};
const maps = new Set(['java.util.HashMap', 'java.util.LinkedHashMap', 'java.util.TreeMap']);
const collections = new Set(['java.util.ArrayList', 'java.util.LinkedList', 'java.util.HashSet',
  'java.util.LinkedHashSet', 'java.util.TreeSet']);
const javaType = name => aliases[name] ?? name.replaceAll('_-', '$');
const fail = message => { throw new Error(message); };

// This reader accepts the XML subset emitted by XStream configs. DTDs and namespaces are rejected.
function readXml(input) {
  const xml = input.replace(/^\uFEFF/, '').replace(/\r\n?/g, '\n');
  const stack = [];
  let root;
  let offset = 0;
  const name = '[A-Za-z_][A-Za-z0-9_.-]*';

  function checkCharacters(text) {
    for (const character of text) {
      const code = character.codePointAt(0);
      if (![9, 10, 13].includes(code) && !(code >= 32 && code <= 0xD7FF)
          && !(code >= 0xE000 && code <= 0xFFFD) && !(code >= 0x10000 && code <= 0x10FFFF)) {
        fail(`Invalid XML character at offset ${offset}`);
      }
    }
    return text;
  }

  function decode(text) {
    const entities = { amp: '&', lt: '<', gt: '>', quot: '"', apos: "'" };
    return checkCharacters(text.replace(/&(?:#x[0-9a-fA-F]+|#[0-9]+|amp|lt|gt|quot|apos);|&/g, entity => {
      if (entity === '&') fail(`Invalid XML entity at offset ${offset}`);
      const key = entity.slice(1, -1);
      if (Object.hasOwn(entities, key)) return entities[key];
      const code = key.startsWith('#x') ? parseInt(key.slice(2), 16) : Number(key.slice(1));
      if (!Number.isInteger(code) || code > 0x10FFFF) fail('Invalid XML character reference');
      return String.fromCodePoint(code);
    }));
  }

  function finish(marker) {
    const end = xml.indexOf(marker, offset);
    if (end < 0) fail(`Unterminated XML construct at offset ${offset}`);
    return end;
  }

  checkCharacters(xml);
  while (offset < xml.length) {
    if (xml.startsWith('<!--', offset)) {
      const end = finish('-->');
      const comment = xml.slice(offset + 4, end);
      if (comment.includes('--') || comment.endsWith('-')) fail('Invalid XML comment');
      offset = end + 3;
    } else if (xml.startsWith('<![CDATA[', offset)) {
      if (!stack.length) fail('CDATA outside the document root');
      const end = finish(']]>');
      stack.at(-1).text += xml.slice(offset + 9, end);
      offset = end + 3;
    } else if (xml.startsWith('<?', offset)) {
      const end = finish('?>');
      const instruction = xml.slice(offset, end + 2);
      if (!new RegExp(`^<\\?${name}(?:\\s|\\?>)`).test(instruction)) fail('Invalid XML processing instruction');
      if (/^<\?xml(?:\s|\?)/i.test(instruction)
          && (offset !== 0 || !instruction.startsWith('<?xml ') && !instruction.startsWith('<?xml\t') && !instruction.startsWith('<?xml\n') || !/^<\?xml\s+version\s*=\s*(["'])1\.0\1(?:\s+encoding\s*=\s*(["'])UTF-8\2)?(?:\s+standalone\s*=\s*(["'])(?:yes|no)\3)?\s*\?>$/i.test(instruction))) {
        fail('Expected an XML 1.0 declaration with UTF-8 encoding');
      }
      offset = end + 2;
    } else if (xml.startsWith('</', offset)) {
      const close = new RegExp(`^</(${name})\\s*>`).exec(xml.slice(offset));
      if (!close || stack.pop()?.name !== close[1]) fail(`Mismatched closing tag at offset ${offset}`);
      offset += close[0].length;
    } else if (xml.startsWith('<!', offset)) {
      fail('DOCTYPE and entity declarations are not supported');
    } else if (xml[offset] === '<') {
      const open = new RegExp(`^<(${name})(?=\\s|/?>)`).exec(xml.slice(offset));
      if (!open) fail(`Invalid opening tag at offset ${offset}`);
      offset += open[0].length;
      const attributes = Object.create(null);
      while (!/^\s*\/?>/.test(xml.slice(offset))) {
        const attribute = new RegExp(`^\\s+(${name})\\s*=\\s*("[^"<]*"|'[^'<]*')`).exec(xml.slice(offset));
        if (!attribute) fail(`Invalid XML attribute at offset ${offset}`);
        const key = attribute[1];
        if (!['class', 'classname', 'isPresent', 'reference'].includes(key)) fail(`Unsupported attribute '${key}'`);
        if (Object.hasOwn(attributes, key)) fail(`Duplicate XML attribute '${key}'`);
        attributes[key] = decode(attribute[2].slice(1, -1).replace(/[\t\n]/g, ' '));
        offset += attribute[0].length;
      }
      const end = /^\s*(\/?)>/.exec(xml.slice(offset));
      offset += end[0].length;
      const node = { name: open[1], attributes, children: [], text: '', parent: stack.at(-1) };
      if (stack.length) stack.at(-1).children.push(node);
      else if (root) fail('Multiple XML roots');
      else root = node;
      if (!end[1]) stack.push(node);
      if (stack.length > 200) fail('Configuration nesting exceeds 200 elements');
    } else {
      const end = xml.indexOf('<', offset);
      const text = xml.slice(offset, end < 0 ? xml.length : end);
      if (text.includes(']]>')) fail('CDATA terminator outside CDATA');
      if (stack.length) stack.at(-1).text += decode(text);
      else if (text.trim()) fail('Text outside the XML root');
      offset += text.length;
    }
  }
  if (!root || stack.length) fail('Missing or unclosed XML root');
  return root;
}

function parseXml(xml) {
  const root = readXml(xml);
  const active = new Set();
  let expanded = 0;
  function expand(node) {
    if (++expanded > 1000000) fail("Expanded configuration exceeds one million elements");
    if (active.has(node)) fail('Circular references cannot be represented as configuration values');
    active.add(node);
    try {
      if (node.attributes.reference !== undefined) {
        if (node.children.length || node.text.trim()) fail('Reference element also contains data');
        let target = node;
        const reference = node.attributes.reference;
        const parts = reference.split('/');
        if (reference.startsWith('/')) {
          target = root;
          parts.shift();
          if (parts.shift() !== root.name) fail(`Invalid absolute reference '${reference}'`);
        }
        for (const part of parts) {
          if (part === '.') continue;
          if (part === '..') { target = target?.parent; }
          else {
            const match = /^([A-Za-z_$][\w.$-]*)(?:\[([1-9]\d*)\])?$/.exec(part);
            if (!match) fail(`Unsupported reference '${reference}'`);
            target = target?.children.filter(child => child.name === match[1])[Number(match[2] ?? 1) - 1];
          }
          if (!target) fail(`Unresolved reference '${reference}'`);
        }
        const value = expand(target);
        return { ...value, name: node.name, attributes: { ...value.attributes,
          class: value.attributes.class ?? (aliases[value.name] || schemas[javaType(value.name)] ? value.name : undefined) } };
      }
      return { name: node.name, attributes: node.attributes, text: node.text, children: node.children.map(expand) };
    } finally { active.delete(node); }
  }
  return expand(root);
}

function scalar(node, type, dynamic) {
  if (node.children.length) fail(`Unexpected children in scalar <${node.name}>`);
  const text = node.text;
  let value = text;
  if (type === 'java.lang.Boolean') {
    if (!['true', 'false'].includes(text)) fail(`Invalid boolean '${text}'`);
    value = text === 'true';
  } else if (['java.lang.Integer', 'java.lang.Long', 'java.lang.Short', 'java.lang.Byte', 'java.math.BigInteger'].includes(type)) {
    if (!/^[+-]?\d+$/.test(text)) fail(`Invalid integer '${text}'`);
    const integer = BigInt(text);
    const limits = { 'java.lang.Byte': [-128n, 127n], 'java.lang.Short': [-32768n, 32767n],
      'java.lang.Integer': [-2147483648n, 2147483647n],
      'java.lang.Long': [-9223372036854775808n, 9223372036854775807n] };
    if (limits[type] && (integer < limits[type][0] || integer > limits[type][1])) fail(`Out-of-range ${type}: ${text}`);
    // Jackson accepts numeric strings for typed long/BigInteger without JS number rounding.
    value = ['java.lang.Long', 'java.math.BigInteger'].includes(type) ? integer.toString() : Number(integer);
  } else if (['java.lang.Double', 'java.lang.Float'].includes(type)) {
    if (!/^[+-]?(?:\d+\.?\d*|\.\d+)(?:[eE][+-]?\d+)?$/.test(text)
        && !['NaN', 'Infinity', '-Infinity'].includes(text)) fail(`Invalid floating-point number '${text}'`);
    value = Number(text);
    if (!Number.isFinite(value)) value = text;
  } else if (type === 'java.math.BigDecimal') {
    if (!/^[+-]?(?:\d+\.?\d*|\.\d+)(?:[eE][+-]?\d+)?$/.test(text)) fail(`Invalid decimal '${text}'`);
  } else if (type === 'java.lang.Character' && text.length !== 1) {
    fail('Character value must contain exactly one UTF-16 code unit');
  }
  if (type === 'java.io.File' || ['java.math.BigInteger', 'java.math.BigDecimal'].includes(type)) return [type, value];
  if (['java.lang.Float', 'java.lang.Double'].includes(type)) return dynamic ? [type, text] : text;
  if (dynamic && !['java.lang.String', 'java.lang.Boolean', 'java.lang.Integer', 'java.lang.Double'].includes(type)) return [type, value];
  return value;
}

function convert(node, declared = 'object') {
  const dynamic = declared === 'object';
  let type = javaType(node.attributes.class ?? (dynamic ? node.name : declared));
  if (node.name === 'null') {
    if (node.children.length || node.text.trim()) fail('Invalid null element');
    return null;
  }
  if (node.children.length && node.text.trim()) fail(`Mixed text and elements in <${node.name}>`);
  if ((maps.has(type) || collections.has(type) || schemas[type] || type === strings) && node.text.trim()) {
    fail(`Unexpected text in container <${node.name}>`);
  }
  if (type.endsWith('-array')) {
    const component = type.slice(0, -6);
    const codes = { string: 'Ljava.lang.String;', object: 'Ljava.lang.Object;', int: 'I', long: 'J',
      boolean: 'Z', short: 'S', byte: 'B', float: 'F', double: 'D', char: 'C' };
    const descriptor = name => name.endsWith('-array') ? '[' + descriptor(name.slice(0,-6))
      : codes[name] ?? (schemas[javaType(name)] || aliases[name] || name === 'java.lang.String'
        ? `L${javaType(name)};` : fail(`Unsupported array component '${name}'`));
    const items = node.children.map(child => convert(child, component));
    return dynamic || component.replaceAll('-array','') === 'object' ? ['[' + descriptor(component), items] : items;
  }
  if ((node.attributes.class ?? node.name) === 'FileList' && node.children.some(child => child.name === 'fileList')) {
    type = 'org.lara.interpreter.joptions.keys.FileList';
  }
  if (type === strings) {
    const items = node.children.length === 1 && node.children[0].name === 'stringList'
      ? node.children[0].children : node.children;
    if (items.some(child => child.name !== 'string')) fail('StringList must contain string elements');
    return [strings, items.map(child => scalar(child, 'java.lang.String', false))];
  }
  if (maps.has(type)) {
    const pairs = node.children.map(entry => {
      if (entry.name !== 'entry' || entry.children.length !== 2 || entry.text.trim()) fail('Map entry must have exactly two children');
      return entry.children.map(child => convert(child));
    });
    const keys = new Set();
    for (const [key] of pairs) {
      const id = JSON.stringify(key);
      if (keys.has(id)) fail(`Duplicate map key ${id}`);
      keys.add(id);
    }
    return [type, pairs];
  }
  if (collections.has(type)) return [type, node.children.map(child => convert(child))];
  if (type === 'java.util.Optional') {
    const present = node.attributes.isPresent;
    if (!['true', 'false'].includes(present)) fail('Optional must have isPresent=true or false');
    let value = { present: present === 'true' };
    if (value.present) {
      if (node.children.length !== 1 || node.children[0].name !== 'value') fail('Present Optional must have one value');
      const inner = node.children[0];
      if (!inner.attributes.classname) fail('Optional value is missing classname');
      const cls = javaType(inner.attributes.classname);
      value.value = convert({ ...inner, attributes: { class: cls } });
      if (value.value === null) fail('Present Optional cannot contain null');
    } else if (node.children.length || node.text.trim()) fail('Empty Optional contains data');
    return dynamic ? [type, value] : value;
  }
  if (enums[type]) {
    if (node.children.length || !enums[type].has(node.text)) fail(`Invalid ${type} constant '${node.text}'`);
    return dynamic ? [type, node.text] : node.text;
  }
  if (type === 'class') return node.text;
  if (schemas[type]) {
    const fields = schemas[type];
    const value = Object.create(null);
    for (const child of node.children) {
      if (!Object.hasOwn(fields, child.name)) fail(`Unsupported field ${type}.${child.name}; refusing to drop it`);
      if (Object.hasOwn(value, child.name)) fail(`Duplicate field ${type}.${child.name}`);
      value[child.name] = convert(child, fields[child.name]);
    }
    const required = {
      [store]: ['name', 'values'], [`${gui}SetupData`]: ['dataset', 'setupName'],
      [`${gui}FieldValue`]: ['rawValue', 'type'],
      'org.suikasoft.jOptions.Options.FileList': ['data'],
      'org.lara.interpreter.joptions.keys.FileList': ['fileList'],
      'org.lara.interpreter.joptions.keys.OptionalFile': ['file', 'used'],
    };
    for (const field of required[type] ?? []) {
      if (!Object.hasOwn(value, field)) fail(`Missing required field ${type}.${field}`);
    }
    if (type === `${gui}ListOfSetups`) {
      const list = value.setupList?.[1];
      const pairs = value.mapOfSetups?.[1];
      if (!list || !pairs) fail('ListOfSetups needs setupList and mapOfSetups');
      const expected = new Map(list.map(setup => [setup[1].setupName, setup]));
      if (pairs.length !== expected.size || pairs.some(([name, setup]) => JSON.stringify(expected.get(name)) !== JSON.stringify(setup))) {
        fail('ListOfSetups mapOfSetups does not match setupList');
      }
      delete value.mapOfSetups;
    }
    return [type, value];
  }
  if (type.startsWith('java.lang.') || ['java.io.File', 'java.math.BigInteger', 'java.math.BigDecimal'].includes(type)) {
    if (!['java.lang.String', 'java.lang.Boolean', 'java.lang.Integer', 'java.lang.Long', 'java.lang.Short',
      'java.lang.Byte', 'java.lang.Double', 'java.lang.Float', 'java.lang.Character', 'java.io.File',
      'java.math.BigInteger', 'java.math.BigDecimal'].includes(type)) fail(`Unsupported scalar type ${type}`);
    return scalar(node, type, dynamic);
  }
  fail(`Unsupported legacy type '${type}'. Custom aliases, enums and converters require manual migration.`);
}

export function convertConfig(xml) {
  const root = parseXml(xml);
  const type = javaType(root.attributes.class ?? root.name);
  if (![store, `${gui}SetupData`].includes(type)) fail('Expected a SimpleDataStore or SetupData configuration root');
  return { format: 'specs-config', version: 1, value: convert(root) };
}

export async function main(args) {
  if (args.length !== 2 || args.some(arg => arg.startsWith('--'))) {
    throw new Error('Usage: node convert-xstream-config.mjs INPUT.xml OUTPUT.json\nThe input is preserved; OUTPUT must not already exist.');
  }
  const [input, output] = args;
  if (resolve(input) === resolve(output)) fail('Input and output must be different files');
  const result = convertConfig(await readFile(input, 'utf8'));
  await writeFile(output, `${JSON.stringify(result, null, 2)}\n`, { flag: 'wx' });
  console.log(`Converted ${input} -> ${output}. Keep the original as a backup.`);
}

if (process.argv[1] && import.meta.url === pathToFileURL(resolve(process.argv[1])).href) {
  main(process.argv.slice(2)).catch(error => { console.error(`Conversion failed: ${error.message}`); process.exitCode = 1; });
}
