// Define a class like array.
const array = function() {
    const a = params[0];
    let index = 0;
    return {
        length: function() {
            let c = 0;
            for (let e in a) c++;
            return c;
        },
        next: function() {
            return a[index++];
        }
    };
};

// Create the array.
const a = array([1,2,3]);

// Print its length.
console.log("Length: " + a.length());

// Print its elements.
console.log("Elements:");
let x = a.next();
while (x) {
    console.log(x);
    x = a.next();
}
