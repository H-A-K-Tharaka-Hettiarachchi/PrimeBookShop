async function register() {


    const fname = document.getElementById("fname");
    const lname = document.getElementById("lname");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const confiremPassword = document.getElementById("confiremPassword");


    const registerDto = {

        fname: fname.value,
        lname: lname.value,
        email: email.value,
        password: password.value,
        confiremPassword: confiremPassword.value

    };
    console.log(registerDto);
    const response = await fetch(
            "Register",
            {
                method: "POST",
                body: JSON.stringify(registerDto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );
    if (response.ok) {
        const json = await response.json();
        if (json.success) {
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
    else {
        Swal.fire({
            title: "Error!",
            text: "Oops Something went wrong",
            icon: "error",
            confirmButtonText: 'Ok'

        });
    }

}


