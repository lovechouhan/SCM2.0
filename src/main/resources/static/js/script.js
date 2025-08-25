
console.log("Script loaded successfully!");


let currentTheme = getTheme();

// initial 

document.addEventListener('DOMContentLoaded', () => {
 changeTheme();

  
});

// console.log(currentTheme);



// TODO
function changeTheme() {
    // set the theme to  webpage
    changePageTheme(currentTheme, currentTheme);

    // set button to the current theme
    const themeButton = document.getElementById("theme_changer");
   
    
    themeButton.addEventListener("click",(event) =>{ 
          const oldTheme = currentTheme;

        console.log("button clicked");
        if (currentTheme === "dark") {
         // setTheme("light");
            currentTheme = "light";
        }else {
        //  setTheme("dark");
            currentTheme = "dark";
        }
      
       changePageTheme(currentTheme,oldTheme)


    });
}



// set theme to localStorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
    document.documentElement.className = theme;
}


// get theme from localStorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    if (theme) {
        return theme;
    } else {
        return "light";
    }
}

// cahnge current page theme
function changePageTheme(theme, oldTheme) {
     // localstorge mein theme ko update karo
        setTheme(currentTheme);

        // remove the old theme class
        document.querySelector("html").classList.remove(oldTheme);
        // add the new theme class
        document.querySelector("html").classList.add(theme);

        // change the button text
        document
        .querySelector("#theme_changer")
        .querySelector("span").textContent = theme === "light" ? "Dark" : "Light";


}

