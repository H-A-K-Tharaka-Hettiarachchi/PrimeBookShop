/* global Swal */

async function addNewBook() {

    //store data from tags
    const bookTitle = document.getElementById("bookTitle");
    const bookMainCategory = document.getElementById("bookMainCategory");
    const bookSubCategory = document.getElementById("bookSubCategory");
    const bookAuthor = document.getElementById("bookAuthor");
    const bookPublishedDate = document.getElementById("bookPublishedDate");
    const bookPublisher = document.getElementById("bookPublisher");
    const bookImage = document.getElementById("bookImage");
    const bookQuantity = document.getElementById("bookQuantity");
    const bookPrice = document.getElementById("bookPrice");
    const bookDescription = document.getElementById("bookDescription");

    //create new form data
    const data = new FormData();
    
    //add data to form data
    data.append("bookTitle", bookTitle.value);
    data.append("bookMainCategoryId", bookMainCategory.value);
    data.append("bookSubCategoryId", bookSubCategory.value);
    data.append("bookAuthorId", bookAuthor.value);
    data.append("bookPublishedDate", bookPublishedDate.value);
    data.append("bookPublisherId", bookPublisher.value);
    data.append("bookQuantity", bookQuantity.value);
    data.append("bookPrice", bookPrice.value);
    data.append("bookDescription", bookDescription.value);
    data.append("bookImage", bookImage.files[0]);



    const response = await fetch(
            "AddProduct",
            {
                method: "POST",
                body: data
            }
    );
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
               Swal.fire({
                title: "Success!",
                text: json.message,
                icon: "success",
                confirmButtonText: 'Ok'

            });
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