// content box
document.addEventListener("DOMContentLoaded", function() {
            setTimeout(function() {
                document.getElementById('contentBox').classList.add('visible');
            }, 1);
        });

// SILEDER FOTO LUPA
document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("imageModal");
    var modalImg = document.getElementById("modalImage");
    var zoomIcons = document.querySelectorAll(".zoom-icon");

    zoomIcons.forEach(function (icon) {
        icon.addEventListener("click", function () {
            modal.style.display = "block";
            var imgSrc = this.previousElementSibling.src;
            modalImg.src = imgSrc;
        });
    });

    var closeBtn = document.querySelector(".modal .close");
    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });

    window.addEventListener("click", function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });
});



document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("productoModal");
    var modalImg = document.getElementById("productModalImage");
    var zoomIcons = document.querySelectorAll(".zoom-icon");

    zoomIcons.forEach(function (icon) {
        icon.addEventListener("click", function () {
            modal.style.display = "block";
            var imgSrc = this.previousElementSibling.src;
            modalImg.src = imgSrc;
        });
    });

    var closeBtn = document.querySelector(".modal .close");
    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });

    window.addEventListener("click", function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });
});

