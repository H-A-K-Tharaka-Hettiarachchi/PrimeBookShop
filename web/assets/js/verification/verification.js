async function verify() {

    const dto = {
        verificationCode: document.getElementById("verificationCode").value
    };



    const response = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );
    if (response.ok) {
        const json = await response.json();
        
        if (json.success) {
            window.location = "index.html";
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
            text: "Oops Something went wrong",
            icon: "error",
            confirmButtonText: 'Ok'

        });
    }



}