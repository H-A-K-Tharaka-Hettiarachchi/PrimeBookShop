
function initializeComponents() {
    console.log("Initializing Components");
    try {
        includeHeaderTop();
    }
    catch (e) {
        console.log(e);
    }
    try {
        includeFooter();
    }
    catch (e) {
        console.log(e);
    }
    try {
        loadAddProduct();
    }
    catch (e) {
        console.log(e);
    }
    try {
        loadProduct();
    }
    catch (e) {
        console.log(e);
    }
    try {
        loadCartItems();
    }
    catch (e) {
        console.log(e);
    }
    try {
        loadCheckout();
    }
    catch (e) {
        console.log(e);
    }
    try {
        loadMainCategories();
    }
    catch (e) {
        console.log(e);
    }

}
async function includeHeaderTop() {

    const response = await fetch("header-top.html");
    if (response.ok) {
        const headerTopHtmlText = await response.text();
        document.getElementById("header-top").innerHTML = headerTopHtmlText;
        checkSignIn();
    }


}

async function includeFooter() {

    const response = await fetch("footer.html");
    if (response.ok) {
        const footerHtmlText = await response.text();
        document.getElementById("footer-container").innerHTML = footerHtmlText;
    }
}

async function checkSignIn() {

    const response = await fetch("CheckingLogedIn");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
//signed in

            let btn_li = document.getElementById("btn-li");
            let login_btn_1 = document.getElementById("login-btn-1");
            login_btn_1.remove();
            let new_logout_btn_1 = document.createElement("a");
            new_logout_btn_1.innerHTML = "LogOut";
            new_logout_btn_1.href = "LogOut";
            btn_li.appendChild(new_logout_btn_1);
        }
        else {
//not signed in
            console.log("Not Signed In");
        }

    }

}
var subCategoryList;
async function loadAddProduct() {

    const response = await fetch(
            "LoadAddProduct",
            );
    if (response.ok) {

        const json = await response.json();
        console.log(json);

        const mainCategoryList = json.mainCategoryList;
        subCategoryList = json.subCategoryList;
        const authorList = json.authorList;
        const publisherList = json.publisherList;

        loadSelect("bookMainCategory", mainCategoryList, "mainCategory");
//        loadSelect("modelSelect", modelList, "name");
        loadSelect("bookAuthor", authorList, "authorName");
        loadSelect("bookPublisher", publisherList, "publisherName");

    }
}



function loadSelect(selectTagId, list, property) {

    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item[property];
        selectTag != null ? selectTag.appendChild(optionTag) : null;
    });
}


function updateSubCategory() {

    let bookSubCategory = document.getElementById("bookSubCategory");
    let selectedBookMainCategoryId = document.getElementById("bookMainCategory").value;
    bookSubCategory.length = 1;
    subCategoryList.forEach(subCategory => {
        if (subCategory.mainCategory.id == selectedBookMainCategoryId) {
            console.log("OKK");
            let optionTag = document.createElement("option");
            optionTag.value = subCategory.id;
            optionTag.innerHTML = subCategory.subCategory;
            bookSubCategory.appendChild(optionTag);
        }
    });
}


