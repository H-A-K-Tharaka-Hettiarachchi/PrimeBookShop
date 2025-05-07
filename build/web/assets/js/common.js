

async function addToCart(pid, qty) {



    const response = await fetch(
            "AddToCart?pid=" + pid + "&qty=" + qty
            );

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            Swal.fire({
                title: "Success!",
                text: json.content,
                icon: "success",
                confirmButtonText: 'Ok'

            });
        }
        else {
            Swal.fire({
                title: "Error!",
                text: json.content,
                icon: "error",
                confirmButtonText: 'Ok'

            });

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