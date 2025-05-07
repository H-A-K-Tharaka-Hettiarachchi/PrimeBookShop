async function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);


    if (parameters.has("pid")) {
        const productId = parameters.get("pid");

        const response = await fetch("LoadShopDetails?pid=" + productId);

        if (response.ok) {

            const json = await response.json();

            //single product view
            const pid = json.product.id;

            document.getElementById("shopDetailsLargeImage").src = "product-images/product" + pid + "/image" + pid + ".png";
            document.getElementById("shopDetailsSmallImage").src = "product-images/product" + pid + "/image" + pid + ".png";
            document.getElementById("shopDetailsTitle").innerHTML = json.product.title;
            document.getElementById("shopDetailsStock").innerHTML = json.product.qty;
            document.getElementById("shopDetailsPrice").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(json.product.price);
            document.getElementById("shopDetailsCategory").innerHTML = "<span >Category: </span> " + json.product.subCategory.mainCategory.mainCategory;
            document.getElementById("shopDetailsPublishedYear").innerHTML = "<span>Publish Years:</span> " + json.product.publishedYear;
            document.getElementById("shopDetailsDescription").innerHTML = json.product.description;


            document.getElementById("addToCartMain").addEventListener(
                    "click",
                    (e) => {
                addToCart(json.product.id,
                        document.getElementById("qty2").value
                        );
                e.preventDefault();
            });
            //single product view


            //related product list listing
            let ProductHtml = document.getElementById("relatedProduct");
            document.getElementById("relatedProductMain").innerHTML = "";

            json.productList.forEach(item => {

                let productCloneHtml = ProductHtml.cloneNode(true);
                productCloneHtml.querySelector("#relatedProductEyeIcon").href = "shop-details.html?pid=" + item.id;
                productCloneHtml.querySelector("#relatedProductImage").src = "product-images/product" + item.id + "/image" + item.id + ".png";
                productCloneHtml.querySelector("#relatedProductImageAnchor").href = "shop-details.html?pid=" + item.id;
                productCloneHtml.querySelector("#relatedProductTitle").innerHTML = item.title;
                productCloneHtml.querySelector("#relatedProductPrice").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(item.price);
                productCloneHtml.querySelector("#relatedProductMainCategory").innerHTML = item.subCategory.mainCategory.mainCategory;

                productCloneHtml.querySelector("#relatedProductAddToCart").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                });
                document.getElementById("relatedProductMain").appendChild(productCloneHtml);
            });
            //related product list listing



        }
        else {
            window.location = "index.html";
        }

    }
    else {
        window.location = "index.html";
    }

}
