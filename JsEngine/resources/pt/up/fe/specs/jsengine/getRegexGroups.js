function jsengine_getRegexGroups(string, regex) {
  const matches = string.matchAll(regex);
  var result = [];
  for (const match of matches) {
    // Return first match
    return match;
    // Ignore first
    //return match.slice(1, match.length);
  }
}
