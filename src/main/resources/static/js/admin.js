console.log("Previewing IMG");

// document
// .querySelector("#image_file_input")
// .addEventListener("change", function(event) {
//     let file = event.target.files[0];

//     let reader = new FileReader();
//     reader.onload = function() {
//         document
//         .querySelector("#imagePreview")
//         .setAttribute("src", reader.result);
//     };
//     reader.readAsDataURL(file);
    
// });



document
  .querySelector("#image_file_input")
  .addEventListener("change", function(event) {
    let file = event.target.files[0];
    if (!file) return; // agar file select hi nahi hui

    let reader = new FileReader();
    reader.onload = function() {
      let img = document.querySelector("#imagePreview");
      img.setAttribute("src", reader.result);
      img.style.display = "block";   // show kar do
    };
    reader.readAsDataURL(file);
  });


// document.addEventListener('DOMContentLoaded', function() {
//   var imageInput = document.getElementById('image_file_input');
//   var imagePreview = document.getElementById('imagePreview');
//   if (imageInput && imagePreview) {
//     imageInput.addEventListener('change', function(event) {
//       const [file] = event.target.files;
//       if (file) {
//         imagePreview.src = URL.createObjectURL(file);
//       }
//     });
//   }
// });
