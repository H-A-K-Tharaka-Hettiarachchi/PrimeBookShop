/* global Swal */

async function loadCartItems() {

    //GET request to load cart items
    const response = await fetch("LoadCartItems");

    if (response.ok) {
        const json = await response.json();

        if (json.length === 0) {
            //cart empty
            Swal.fire({
                title: "Info!",
                text: "Cart is Empty",
                icon: "info",
                confirmButtonText: 'Ok'

            });
        }
        else {

            let  cartItemContainer = document.getElementById("cartItemContainer");
            let  cartItemRow = document.getElementById("cartItemRow");

            cartItemContainer.innerHTML = "";


            let cartSubTotal = 0;
            let cartTotal = 0;

            json.forEach(item => {

                let itemSubTotal = (item.product.price * item.qty);
                cartSubTotal += (item.product.price * item.qty);
                cartTotal += itemSubTotal;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cartItemTitle").href = "shop-details.html?pid=" + item.product.id;
                cartItemRowClone.querySelector("#cartItemImage").src = "product-images/product" + item.product.id + "/image" + item.product.id + ".png";
                cartItemRowClone.querySelector("#cartItemTitle").innerHTML = item.product.title;
                cartItemRowClone.querySelector("#cartItemPrice").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(item.product.price);
                cartItemRowClone.querySelector("#qty2").value = item.qty;
                cartItemRowClone.querySelector("#cartItemSubTotal").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(itemSubTotal);


                cartItemRowClone.querySelector("#qty2").addEventListener(
                        "change",
                        (e) => {
                    updateCartQty(item.product.id, cartItemRowClone.querySelector("#qty2").value);
                    e.preventDefault();
                });

                cartItemContainer.appendChild(cartItemRowClone);

            });

            document.getElementById("cartSubTotal").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(cartSubTotal);
            document.getElementById("cartTotal").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(cartTotal);

        }


    }
    else {
        Swal.fire({
            title: "Error!",
            text: "Oops something went wrong",
            icon: "error",
            confirmButtonText: 'Ok'

        });
    }

}


async function updateCartQty(pid, qty) {

    const response = await fetch("UpdateCartQty?pid=" + pid + "&qty=" + qty);
    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            Swal.fire({
                title: "Success!",
                text: json.message,
                icon: "success",
                confirmButtonText: 'Ok'

            });
            location.reload();
        }
        else {

            Swal.fire({
                title: "Error!",
                text: json.message,
                icon: "error",
                confirmButtonText: 'Ok'

            });

        }
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