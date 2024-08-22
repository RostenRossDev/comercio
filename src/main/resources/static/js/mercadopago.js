const mp = new MercadoPago('TEST-878f7a37-a6a4-49b2-86d4-7f42f99412a7', { //public key
    locale:'es-AR'
});

let MPPreferenceId;

const MP = async () => {
    try {
        console.log("1")
        const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
        const datosEnvio = JSON.parse(sessionStorage.getItem('datosEnvio')) || [];

        const orderData = {
            items: cart.map(item => ({
               name: item.name,
               id: item.id,
               price: item.price.toString(), // Convertir a String para el formato esperado
               discount: item.discount,
               quantity: item.quantity,
               delivery:datosEnvio
            })),
            envio: datosEnvio
        };

        console.log("2")
        const response = await fetch("/create_preference", {
            method: "POST",
            headers: {
                "Accept" : "application/json",

                "Content-Type": "application/json",
            },
            body: JSON.stringify(orderData),
        });
        console.log("3")
        const preference = await response.text(); // Asumiendo que la respuesta es un JSON
        console.log("4")
        createCheckoutButton(preference); // Usar el ID de la preferencia
        console.log("preference: ", preference);
    } catch (error) {
        console.log(error)
        alert("error: " + error);
    }
};

const createCheckoutButton = (preferenceId) => {
    console.log("5")

    const bricksBuilder = mp.bricks();
    console.log("6")

    const generateButton = async () => {
        console.log("7")

        if (window.checkoutButton){
            console.log("7.5")
            window.checkoutButton.unmount();
        }

        console.log("8")
        MPPreferenceId = preferenceId;
        console.log("MPPreferenceId: " + MPPreferenceId)
        window.checkoutButton = bricksBuilder.create("wallet", "wallet_container", {
            initialization: {
                preferenceId: preferenceId,
            },
             callbacks: {
                onReady: () => {
                    document.getElementById('wallet_container').addEventListener('click', processPayment);
                }
            },
        });
    };

    console.log("9")

    generateButton();
};

const processPayment = async () => {
    // Obtén los datos del envío desde sessionStorage
    const envio = JSON.parse(sessionStorage.getItem('datosEnvio'));
    const cartItems = JSON.parse(sessionStorage.getItem('cart'));

    // Construir el objeto BuyerData basado en los datos obtenidos
    const buyerData = {
        preferenceId: MPPreferenceId,
        nombre: envio.nombre,
        apellido: envio.apellido,
        email: envio.email,
        calle: envio.calle,
        altura: parseInt(envio.altura),
        barrio: envio.barrio,
        casa: envio.casa,
        departamento: envio.departamento,
        piso: envio.piso,
        entreCalles: envio.entreCalles,
        telefono: envio.telefono,
        descripcion: envio.descripcion,
        retiroLocal: envio.rangoEntrega,
        items: cartItems.map(item => ({
            id: item.id,
            title: item.name,
            quantity: item.quantity,
            unit_price: item.price,
            discount: item.discount,
        }))
    };

    try {
        // Envía la solicitud al controlador de Spring Boot
        const response = await fetch('/fill-venta', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(buyerData),
        });

        if (!response.ok) {
            throw new Error('Error al procesar la compra');
        }

        const data = await response.json();
        console.log('Compra procesada correctamente', data);
    } catch (error) {
        console.error('Error al enviar la solicitud:', error);
    }
};


/* #####################  carrito */

function addToCart(name, id, price, discount) {
console.log("agregando al carro: " + name + ", " + id + ", " + price + ", " + discount)
    fetch('/addToCart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'name': name,
            'id': id,
            'price': price,
            'discount': discount
        })
    }).then(() => {
        updateCartIcon();
    });
}
document.addEventListener('DOMContentLoaded', (event) => {
    MP();
});


