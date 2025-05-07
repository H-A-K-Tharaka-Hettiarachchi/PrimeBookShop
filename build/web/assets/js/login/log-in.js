async function logIn() {

    const loginDto = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        remeberme: document.getElementById("saveForNext").checked
    };
    const response = await fetch(
            "LogIn",
            {
                method: "POST",
                body: JSON.stringify(loginDto),
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
            if (json.content === "UnVerified") {
                window.location = "verification.html";
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