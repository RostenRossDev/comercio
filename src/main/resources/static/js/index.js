let cart = JSON.parse(sessionStorage.getItem('cart')) || [];

function addToCart(name, id, price, discount) {
    let carts = JSON.parse(sessionStorage.getItem('cart')) || [];

    const existingItem = carts.find(item => item.id === id);

    if (!existingItem) {
        Toastify({
            text: "Item agregado al carrito",
            duration: 3000,
            close: true,
            gravity: "bottom", // "top" or "bottom"
            position: "right", // "left" or "right"
            backgroundColor: "#4CAF50", // Green color
            stopOnFocus: true // Prevents dismissing of toast on hover
        }).showToast();
        carts.push({ name, id, price, discount, quantity: 1 });
        sessionStorage.setItem('cart', JSON.stringify(carts));
        console.log(JSON.stringify(cart))
        console.log(cart)
        updateCartIcon();
    }else {
        Toastify({
            text: "El item ya esta en el carrito",
            duration: 3000,
            close: true,
            gravity: "bottom", // "top" or "bottom"
            position: "right", // "left" or "right"
            backgroundColor: "#c92626", // red color
            stopOnFocus: true // Prevents dismissing of toast on hover
        }).showToast();
    }
}

function calculateTotal() {
    return cart.reduce((total, item) => total + (item.price * (1 - item.discount / 100)) * item.quantity, 0);
}

function sendCartToServer() {
    const cartData = JSON.parse(sessionStorage.getItem('cart')) || [];
    if(cartData.length > 0){
        let cartJSON = JSON.stringify(cartData);
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/detalle-de-Compra';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'cart';
        input.value = cartJSON;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const cartData = JSON.parse(sessionStorage.getItem('cart')) || [];
    cart = cartData;
    updateCartIcon();
});

function updateCartIcon(){
    const cartData = JSON.parse(sessionStorage.getItem('cart')) || [];
    let quantity = 0;
    if(cartData != null && cartData.length > 0){
        quantity += cartData.reduce((total, item) => total + item.quantity, 0);
    }
    console.log("quantity: " + quantity)
    let totalDisplay = document.getElementsByClassName('cartQuantity');
    console.log("totalDisplay: " +totalDisplay)
    if (totalDisplay) {
        totalDisplay[0].textContent = quantity;
        totalDisplay[1].textContent = quantity;
    }
}

// Llamar a la función al cargar la página
document.addEventListener('DOMContentLoaded', (event) => {
    updateCartIcon();
});


// ############################# modal idex
function changeSecundaryText(){
    Swal.fire({
        title: 'Nuevo texto para el sitio Web',
        input: 'text',
        inputLabel: 'Texto del Sitio Web',
        inputPlaceholder: 'Ingrese un nuevo texto para el sitio web',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un texto!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Texto del sitio web guardado', 'success');
        }
    });
}

function changeTitleText(){
    Swal.fire({
        title: 'Nuevo titulo para el sitio Web',
        input: 'text',
        inputLabel: 'Titulo del Sitio Web',
        inputPlaceholder: 'Ingrese un nuevo titulo para el sitio web',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un texto!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Texto del sitio web guardado', 'success');
        }
    });
}

function changeProductBotonText(){
    Swal.fire({
        title: 'Nuevo texto para el boton de productos',
        input: 'text',
        inputLabel: 'Texto para el boton de productos',
        inputPlaceholder: 'Ingrese un nuevo texto para el boton',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un texto!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Texto del boton productos', 'success');
        }
    });
}

function changeSaleTitle(){
    Swal.fire({
        title: 'Nuevo texto para la sección de ofertas',
        input: 'text',
        inputLabel: 'Sección de ofertas',
        inputPlaceholder: 'Ingrese un nuevo texto para las ofertas',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un texto!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Texto de ofertas', 'success');
        }
    });
}

function changeProductsTitle(){
    Swal.fire({
        title: 'Nuevo texto para la sección productos',
        input: 'text',
        inputLabel: 'Sección de productos',
        inputPlaceholder: 'Ingrese un nuevo texto para los productos',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un texto!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Texto de productos', 'success');
        }
    });
}