/* global Swal */

async function loadProduct() {

    const response = await fetch("LoadShopDetails?pid=shopLoad");

    if (response.ok) {


        const json = await response.json();

        //shop product list listing
        let ProductHtml = document.getElementById("productItem");
        document.getElementById("productItemContainer").innerHTML = "";

        json.productList.forEach(item => {

            let productCloneHtml = ProductHtml.cloneNode(true);
            productCloneHtml.querySelector("#productItemVeiwProduct").href = "shop-details.html?pid=" + item.id;
            productCloneHtml.querySelector("#productItemImage").src = "product-images/product" + item.id + "/image" + item.id + ".png";
            productCloneHtml.querySelector("#productItemImageAnchor").href = "shop-details.html?pid=" + item.id;
            productCloneHtml.querySelector("#productItemTitle").innerHTML = item.title;
            productCloneHtml.querySelector("#productItemPrice").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(item.price);


            productCloneHtml.querySelector("#productItemAddToCart").addEventListener(
                    "click",
                    (e) => {
                addToCart(item.id, 1);
                e.preventDefault();
            });
            document.getElementById("productItemContainer").appendChild(productCloneHtml);
        });
        //shop product list listing



    }
    else {
        Swal.fire({
            title: "Error!",
            text: "Oops Something went wrong",
            icon: "error",
            confirmButtonText: 'Ok'

        });
    }

}


async function loadMainCategories() {

    const response = await fetch("LoadMainCategories");

    if (response.ok) {


        const json = await response.json();

        //shop product list listing
        let ProductHtml = document.getElementById("mainCategoryItem");
        document.getElementById("mainCategoryContainer").innerHTML = "";

        json.mainCategoryList.forEach(item => {

            let productCloneHtml = ProductHtml.cloneNode(true);
            productCloneHtml.querySelector("#mainCategoryItemButton").innerHTML = item.mainCategory;

            productCloneHtml.querySelector("#mainCategoryItemButton").addEventListener(
                    "click",
                    (e) => {
                selectCategory(item.id);
                e.preventDefault();
            });
            document.getElementById("mainCategoryContainer").appendChild(productCloneHtml);
        });
        //shop product list listing



    }


}


function selectCategory(id) {
    console.log(id);
}