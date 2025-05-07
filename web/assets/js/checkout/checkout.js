/* global Swal, payhere */

// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {
    Swal.fire({
        title: "Success!",
        text: "Order Placed. Thank you",
        icon: "success",
        confirmButtonText: 'Ok'

    });
    window.location = "index.html";
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};

var provinceListGlobal;
var districtListGlobal;
var cityListGlobal;

async function loadCheckout() {



    const response = await fetch(
            "LoadCheckout",
            );
    if (response.ok) {
        const  json = await response.json();
//        console.log(json);
        if (json.success) {

            if (json.message != null) {
                Swal.fire({
                    title: "Error!",
                    text: json.message,
                    icon: "error",
                    confirmButtonText: 'Ok'

                });
            }

            //store response data
            const address = json.address;
            const provinceList = json.provinceList;
            const districtList = json.districtList;
            const cityList = json.cityList;
            const cartItemList = json.cartItemList;



            console.log(json);

            //load province
            const provinceSelectTag = document.getElementById("province");
            json.provinceList.forEach(item => {
                let optionTag = document.createElement("option");
                optionTag.value = item.id;
                optionTag.innerHTML = item.province;
                provinceSelectTag.appendChild(optionTag);
            });
            //load districts
            const districtSelectTag = document.getElementById("district");
            json.districtList.forEach(item => {
                let optionTag = document.createElement("option");
                optionTag.value = item.id;
                optionTag.innerHTML = item.district;
                districtSelectTag.appendChild(optionTag);
            });
            //load cities
            const citySelectTag = document.getElementById("city");
            cityList.forEach(item => {
                let optionTag = document.createElement("option");
                optionTag.value = item.id;
                optionTag.innerHTML = item.city;
                citySelectTag.appendChild(optionTag);
            });

            //check address status
            let isDifferentAddress = document.getElementById("saveForNext2");
            isDifferentAddress.addEventListener("change", e => {



                let firstName = document.getElementById("firstName");
                let lastName = document.getElementById("lastName");
                let addressLine1 = document.getElementById("addressLine1");
                let addressLine2 = document.getElementById("addressLine2");
                let province = document.getElementById("province");
                let district = document.getElementById("district");
                let city = document.getElementById("city");
                let postalCode = document.getElementById("postalCode");
                let mobile = document.getElementById("mobile");
//                let savePayment = document.getElementById("savePayment");

                if (isDifferentAddress.checked) {
                    firstName.value = "";
                    lastName.value = "";
                    addressLine1.value = "";
                    addressLine2.value = "";
                    postalCode.value = "";
                    mobile.value = "";
                    province.value = 0;
                    province.disabled = false;
                    district.value = 0;
                    district.disabled = false;
                    city.value = 0;
                    city.disabled = false;
                    city.dispatchEvent(new Event("change"));
                }
                else {
                    try {
                        firstName.value = address.firstName;
                        lastName.value = address.lastName;
                        addressLine1.value = address.line1;
                        addressLine2.value = address.line2;
                        postalCode.value = address.postalCode;
                        mobile.value = address.mobile;
                        province.value = address.city.district.province.id;
                        province.disabled = true;
                        district.value = address.city.district.id;
                        district.disabled = true;
                        city.value = address.city.id;
                        city.disabled = true;
                        city.dispatchEvent(new Event("change"));
                    }
                    catch (e) {
                        console.log(e);
                    }


                }
            });

            let  cartItemContainer = document.getElementById("cartItemContainer");
            let cartItemRow = document.getElementById("cartItemRow");


            cartItemContainer.innerHTML = "";
            let subTotal = 0;





            cartItemList.forEach(item => {

                let itemSubTotal = (item.product.price * item.qty);
                subTotal += itemSubTotal;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cartItemTitle").innerHTML = item.product.title + " X (" + item.qty + ")";
                cartItemRowClone.querySelector("#cartItemPrice").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(itemSubTotal);

                cartItemContainer.appendChild(cartItemRowClone);
            });

            document.getElementById("subTotal").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(subTotal);



            provinceSelectTag.addEventListener("change", e => {

                let itemCount = json.cartItemList.length;
                let shipping = 0;
                shipping = provinceSelectTag.value == 9 ? 1000 * itemCount : 2000 * itemCount;
                let total = shipping + subTotal;

                document.getElementById("shipping").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(shipping);

                document.getElementById("total").innerHTML = new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(total);

            });
            document.getElementById("city").dispatchEvent(new Event("change"));
            document.getElementById("saveForNext2").dispatchEvent(new Event("change"));
            document.getElementById("province").dispatchEvent(new Event("change"));

            provinceListGlobal = provinceList;
            districtListGlobal = districtList;
            cityListGlobal = cityList;

        }
        else {
            Swal.fire({
                title: "Error!",
                text: json.message,
                icon: "error",
                confirmButtonText: 'Ok'

            });
//            window.location = "login.html";
        }
    }
    else {
        Swal.fire({
            title: "Error!",
            text: "Oops Somthing went wrong",
            icon: "error",
            confirmButtonText: 'Ok'

        });
    }
}
function updateDistrict() {

    let districtSelectTag = document.getElementById("district");
    let selectedProvinceId = document.getElementById("province").value;
    districtSelectTag.length = 1;

    let citySelectTag = document.getElementById("city");
    citySelectTag.length = 1;

    try {
        districtListGlobal.forEach(district => {
            if (district.province.id == selectedProvinceId) {
                let optionTag = document.createElement("option");
                optionTag.value = district.id;
                optionTag.innerHTML = district.district;
                districtSelectTag.appendChild(optionTag);
            }
        });
    }
    catch (e) {
        console.log(e);
    }


}

function updateCity() {

    let citySelectTag = document.getElementById("city");
    let selectedDistrictId = document.getElementById("district").value;
    citySelectTag.length = 1;

    try {
        cityListGlobal.forEach(city => {
            if (city.district.id == selectedDistrictId) {
                let optionTag = document.createElement("option");
                optionTag.value = city.id;
                optionTag.innerHTML = city.city;
                citySelectTag.appendChild(optionTag);
            }
        });
    }
    catch (e) {
        console.log(e);
    }


}

async function checkout() {


    //check address status
    let isDifferentAddress = document.getElementById("saveForNext2");

    //get address data
    let firstName = document.getElementById("firstName");
    let lastName = document.getElementById("lastName");
    let addressLine1 = document.getElementById("addressLine1");
    let addressLine2 = document.getElementById("addressLine2");
    let province = document.getElementById("province");
    let district = document.getElementById("district");
    let city = document.getElementById("city");
    let postalCode = document.getElementById("postalCode");
    let mobile = document.getElementById("mobile");
//    let savePayment = document.getElementById("savePayment");

    //request data(json)
    const data = {
        isDifferentAddress: isDifferentAddress.checked,
        firstName: firstName.value,
        lastName: lastName.value,
        addressLine1: addressLine1.value,
        addressLine2: addressLine2.value,
        postalCode: postalCode.value,
        mobile: mobile.value,
        province: province.value,
        district: district.value,
        city: city.value

    };

    const response = await fetch(
            "Checkout",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.success) {

            console.log(json.payhereJson);
            payhere.startPayment( json.payhereJson);
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
//        "Oops Somthing went wrong"
        Swal.fire({
            title: "Error!",
            text: "Try again later",
            icon: "error",
            confirmButtonText: 'Ok'

        });
        console.log("Try again later");
    }

}
