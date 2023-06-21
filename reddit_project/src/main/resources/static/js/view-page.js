const allReplyBtns = document.querySelectorAll(".comment-reply-link");

allReplyBtns.forEach((btn, index) =>
    btn.addEventListener("click", (e) => {
        e.preventDefault();
        const formDiv = document.querySelectorAll(".add-reply")[index];
        if (formDiv.style.display === "none") {
            formDiv.style.display = "block";
        } else {
            formDiv.style.display = "none";
        }
    })
);
