

document.addEventListener("DOMContentLoaded", () => {
    console.log('hello world');
    const baseURL = "http://localhost:8080";
    //const baseURL = "https://contact-manager-scm.up.railway.app";
    // declared a varaible for base URL

    const viewContactModel = document.getElementById("viewcontact");

    const options = {
        placement: 'bottom-right',
        backdrop: 'dynamic',
        backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
        closable: true,
        onHide: () => console.log('modal is hidden'),
        onShow: () => console.log('modal is shown'),
        onToggle: () => console.log('modal has been toggled'),
    };

    const instanceOptions = {
      id: 'viewcontact',
      override: true
    };

    // make it global so HTML button onclick can access it
    window.contactModal = new Modal(viewContactModel, options, instanceOptions);

    // for opening of contact modal
    window.openContactModal = function() {
        contactModal.show();
    }

    // for closing of contact modal
    window.closeContactModal = function() {
        contactModal.hide();
    }

    window.loadContactData = async function(userId) {
        // Load user data using the userId
        console.log('Loading user data for:', userId);

       try{

        const data  = await (await fetch(`${baseURL}/api/contacts/${userId}`)).json();
        console.log('User data loaded:', data);


        // setting contact information by replacing terms and condition block in ContactModelAPI.html
        document.querySelector('#contact_name').innerHTML = data.name;
        document.querySelector('#contact_email').innerHTML = data.email;
        document.querySelector('#contact_phone').innerHTML = data.phone;
        document.querySelector('#contact_image').src = data.picture;
    
        document.querySelector('#contact_address_table').innerHTML = data.address;
        document.querySelector('#contact_about_table').innerHTML = data.description;

        document.querySelector('#contact_website_table').href = data.websiteLink;
        document.querySelector('#contact_website_table').innerHTML = data.websiteLink;
        document.querySelector('#linkedintableLink').href = data.linkedinLink;
        document.querySelector('#linkedintableLink').innerHTML = data.linkedinLink;
        const favouriteContact =  document.querySelector('#contact_favourite_table');

       if(data.favorite){
        //    favouriteContact.innerHTML = "<i class='fas fa-star text-yellow-500'></i> This Contact is in Your Favourite";
              favouriteContact.innerHTML = "<i class='fa-solid fa-heart text-red-500'></i> This Contact is in Your Favourite";
       } else {
           favouriteContact.innerHTML = 'Not in Your Favourite';
       }

        openContactModal();
        
       } catch(error){
        console.log('Error loading user data:', error);
       }
        console.log(userId);
             
    }

    // for deleting alert of contacts
    window.deleteContact = async function(contactId) {
     
     // sweet alert ui
     
Swal.fire({
    title: "Do you want to Delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
    cancelButtonText: "Cancel",
     
}).then((result) => {
    if (result.isConfirmed) {
        const url = `${baseURL}/user/contact/delete/${contactId}`;
        window.location.replace(url);
    }
});



    }

});


// // src/main/resources/static/js/contactViewing.js

// console.log('hello world');

//         const viewContactModel = document.getElementById("viewcontact");
        
// // options with default values
// const options = {
//     placement: 'bottom-right',
//     backdrop: 'dynamic',
//     backdropClasses:
//         'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
//     closable: true,
//     onHide: () => {
//         console.log('modal is hidden');
//     },
//     onShow: () => {
//         console.log('modal is shown');
//     },
//     onToggle: () => {
//         console.log('modal has been toggled');
//     },
// };

// // instance options object
// const instanceOptions = {
//   id: 'viewcontact',
//   override: true
// };

// const contactModal = new Modal(viewContactModel, options, instanceOptions);

// function openContactModal() {
//     contactModal.show();
// }

// src/main/resources/static/js/contactViewing.js
