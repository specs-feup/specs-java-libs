/**
 * Implementation of console.log according to Mozilla: https://developer.mozilla.org/en-US/docs/Web/API/Console/log
 */
console.log = function () {
  // Return if no args
  if (arguments.length === 0) {
    return;
  }

  // In the future, should this be configurable?
  //var outStream = Java.type("java.lang.System").out;

  // When there is only one argument
  var msg = arguments[0];
  if (arguments.length === 1) {
    outStream.print(msg.toString());
    return;
  }

  // If first argument is a string, interpret remaining args as substitution strings
  if (typeof msg === "string" || msg instanceof String) {
    var subst = [];
    for (var i = 1; i < arguments.length; i++) {
      subst.push(arguments[i]);
    }

    outStream.printf(msg.toString(), subst);

    return;
  }

  // Concatenate all arguments
  for (var i = 1; i < arguments.length; i++) {
    msg = msg + arguments[i].toString();
  }

  outStream.print(msg);
};

/**
 * Implementation of print() as alias of console.log()
 */
print = console.log;
