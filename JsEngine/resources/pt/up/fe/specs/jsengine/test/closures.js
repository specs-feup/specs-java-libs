    var a = [];
    
    //println("a: " + a['push']);
    a['push'](10);
    
    //var f1 = a['push'];
    //println("f1: " + f1);
    //f1(a, 10);

    //println(typeof getter(a, 'push'));
    getter(a, 'push')(20);
    
    //println(a);
    
function getter(obj, prop) {
    if(typeof obj[prop] === 'function') {
        //println("Creating function"); 
        return function() {
            //println("Executing function: " + arguments);
            obj[prop].apply(obj, arguments);
        };
    }

    return obj[prop];
}   

a; 