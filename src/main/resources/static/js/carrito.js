function fetchUpdateCartQuantity(productId, newQuantity) {
    return fetch('/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}&quantity=${newQuantity}`,
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 409) { // Conflicto detectado
            return null; // Devolver null para indicar que hay un conflicto
        } else {
            throw new Error("Error al actualizar la cantidad del producto");
        }
    });
}

function fetchProductDetails(productId) {
    return fetch(`/product/details/${productId}`)
        .then(response => response.json())
        .catch(error => {
            console.error("Error al obtener los detalles del producto:", error);
            return null;
        });
}

function updateCartIcon() {
    const carts = JSON.parse(sessionStorage.getItem('cart')) || [];

    const cartIcon = document.querySelector('.cart-icon img');
    cartIcon.alt = `Carrito (${carts.length})`;
}

function updateQuantity(productId, change) {
    console.log("productId: " + productId)
    // Encuentra el producto en el carrito
    const carts = JSON.parse(sessionStorage.getItem('cart')) || [];
    console.log("carts: " + JSON.stringify(carts))

        console.log("productId: " + productId)
    let product = carts.find(item =>{
        console.log(JSON.stringify(item))
        return item.id === productId;
    });

    console.log("product: " +JSON.stringify(product));

    if (product) {
        // Actualiza la cantidad, asegurándose de que no sea menor a 1
        const sum =  product.quantity + change;
        const newQuantity = Math.max(1, sum);
        console.log("quantity: " + newQuantity);

        // Realizar la llamada al controlador
         fetchUpdateCartQuantity(productId, change)
        .then(updatedProduct => {
            console.log("updatedProduct: " + updatedProduct)
            if (updatedProduct) {
                let newCart = carts.map(item =>{
                   console.log("item: " + JSON.stringify(item))

                   if(item.id == productId){
                       console.log("INGRESAMOS AL IF: " + JSON.stringify(item))
                       item.quantity  = newQuantity;
                   }
                   return item;
                });
               console.log("updatedProduct em eL IF: " + JSON.stringify(newCart))
               console.log("updatedProduct em eL IF: " + newCart)
               let stock = document.getElementById(`stock-` + productId);
               // Actualiza la visualización de la cantidad y el total
               let quantityDisplay = document.getElementById(`quantity-` + productId);
               if(updatedProduct.stock > 0){
                   if (quantityDisplay) {
                       quantityDisplay.textContent = newQuantity;
                       stock.textContent = updatedProduct.stock - 1;
                       sessionStorage.setItem('cart', JSON.stringify(newCart));
                   }
               }
               updateCartTotal();
               updateItemCartTotal(productId);
           } else {
               alert("Hubo un conflicto al actualizar el producto. Se recargará la información.");
               // Reintenta la actualización después de obtener los datos más recientes del producto
               fetchProductDetails(productId)
                   .then(latestProduct => {
                       if (latestProduct) {
                           product.quantity = latestProduct.quantity;
                           updateQuantity(productId, 1); // Intenta actualizar nuevamente
                       }
                   });
           }
        })
        .catch(error => {
           console.error("Error al actualizar la cantidad del producto en el carrito:", error);
        });

        // Actualiza la visualización de la cantidad y el total
//        let quantityDisplay = document.getElementById(`quantity-` + productId);
//        if (quantityDisplay) {
//            quantityDisplay.textContent = product.quantity;
//        }
//        updateCartTotal();
//        updateItemCartTotal(productId);
    }
}


function chargeQuantity() {
    // Encuentra el producto en el carrito
    const carts = JSON.parse(sessionStorage.getItem('cart')) || [];
    console.log("carts: " + JSON.stringify(carts))

    carts.forEach(product => {
        // Actualiza la cantidad, asegurándose de que no sea menor a 1
        console.log("quantity: " + product.quantity);
        // Actualiza la visualización de la cantidad y el total
        let quantityDisplay = document.getElementById(`quantity-` + product);
        if (quantityDisplay) {
            quantityDisplay.textContent = product.quantity;
        }
    });

}

function updateCartTotal() {
    let carts = JSON.parse(sessionStorage.getItem('cart')) || [];
    let total = carts.reduce((sum, item) => sum + (item.price * (1 - item.discount / 100) * item.quantity), 0);
    console.log("total: " + total);
    let totalDisplay = document.getElementById('cart-total');
    if (totalDisplay) {
        totalDisplay.textContent = total.toFixed(2);
        updateCartIcon();
    }
}

function updateItemCartTotal(itemId) {
    let carts = JSON.parse(sessionStorage.getItem('cart')) || [];
    console.log(JSON.stringify(carts))
    let filteredItems = carts.filter(item => item.id === itemId);
    console.log(JSON.stringify(filteredItems))
    let total = filteredItems.reduce((sum, item) => sum + (item.price * (1 - item.discount / 100) * item.quantity), 0);
    let totalDisplay = document.getElementById('item-total-' + itemId);
    if (totalDisplay) {
        totalDisplay.textContent = total.toFixed(2);
        updateCartIcon();
    }
}

function removeCartItem(id) {

   // Obtener el carrito del sessionStorage
      let carts = JSON.parse(sessionStorage.getItem('cart')) || [];
      console.log("id: " + id)
      console.log("datos viejos: " + carts)
      // Filtrar el carrito para eliminar el elemento con el id especificado
      carts = carts.filter(item => item.id !== id);
      console.log("datos nuevos: " + carts)

      // Actualizar el carrito en el sessionStorage
      sessionStorage.setItem('cart', JSON.stringify(carts));
      let updatedCarts = JSON.parse(sessionStorage.getItem('cart'));
      console.log("Datos guardados en sessionStorage: " + JSON.stringify(updatedCarts));

      // Eliminar el elemento visualmente del DOM
      let itemElement = document.getElementById('item-' + id);
      if (itemElement) {
          itemElement.remove();
      }
      // También puedes actualizar el total si es necesario
     updateCartTotal();
}

document.addEventListener('DOMContentLoaded', function() {
    // Función que verifica el carrito en sessionStorage
    function checkCartAndRedirect() {
        // Obtener el carrito del sessionStorage
        let carts = JSON.parse(sessionStorage.getItem('cart')) || [];

        // Verificar si el carrito tiene 1 o menos elementos
        if (carts.length < 1) {
           const form = document.createElement('form');
               form.method = 'GET';
               form.action = '/inicio';
               document.body.appendChild(form);
               form.submit();
        }
    }

    // Llamar a la función al cargar la página
    checkCartAndRedirect();
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

document.addEventListener('DOMContentLoaded', (event) => {
    console.log("actualizando el numero " )
    updateCartIcon();
    updateCartTotal();
    chargeQuantity();
    const cartData = JSON.parse(sessionStorage.getItem('cart')) || [];
    cartData.forEach(item => {
        console.log("actualizando el numero " + item.id + ", " + item.quantity)
        updateItemCartTotal(item.id)
    })
});

/* #################################### Pago de carrito */
 document.getElementById("btnStep1").addEventListener("click", function() {
    showStep("step2", "step1");

});

document.getElementById("btnStep2").addEventListener("click", function() {
    showStep("step3", "step2");
});



function showPickupDetails() {

    document.getElementById("pickup-details").classList.remove('hidden');
    document.getElementById("delivery-details").classList.add('hidden');
}

function showDeliveryDetails() {
    document.getElementById("pickup-details").classList.add('hidden');
    document.getElementById("delivery-details").classList.remove('hidden');
    // Add logic to calculate and add the delivery fee to the total amount
}

function showStep(stepToShow, currentStep, currentButon, buttonToShow) {
    console.log("stepToShow:", stepToShow, "currentStep:", currentStep);

    console.log("currentButon:", currentButon, "buttonToShow:", buttonToShow);

    // Validar si los elementos existen
    let currentStepElement = document.getElementById(currentStep);
    let stepToShowElement = document.getElementById(stepToShow);
    let currentButonElement = document.getElementById(currentButon);
    let buttonToShowElement = document.getElementById(buttonToShow);
    if (!currentStepElement || !stepToShowElement /*|| !currentButonElement || !buttonToShowElement*/) {
        console.error("Uno de los pasos no existe en el DOM:", currentStep, stepToShow);
        return;
    }
    let pickupDetailElement;
    console.log("stepToShow:", stepToShow, "currentStep:", currentStep);

    console.log(JSON.stringify(currentStepElement))

    console.log(JSON.stringify(stepToShowElement))

    console.log(JSON.stringify(currentButonElement))

    console.log(JSON.stringify(buttonToShowElement))

    if(stepToShow == "step2"){
        pickupDetailElement = document.getElementById("pickup-details");
        pickupDetailElement.classList.remove('hidden');
        settearForm();
    }else if(stepToShow == "step3"){

        if (!retiroLocal && !validarFormulario()) {
            console.log("acaaa")
            //event.preventDefault(); // Evita que se envíe el formulario
            //showStep("step3", "step2", "btnStep2", "wallet_container")
            return;
        }
        let pickupElement = document.getElementById("pickup-details");
        let deliveryDetailsElement = document.getElementById("delivery-details");
        pickupElement.classList.add('hidden');
        deliveryDetailsElement.classList.add('hidden');
        pickupElement.classList.remove('checkout-step');
        deliveryDetailsElement.classList.remove('checkout-step');
        pickupElement.classList.remove('active');
        deliveryDetailsElement.classList.remove('active');
        console.log("CretiroLocal: "  + retiroLocal)

         if (!retiroLocal) {
            console.log("Cargando formulario de envio")
            datosEnvio = cargarFormEnvio();
            console.log(JSON.stringify(datosEnvio))
         }



         console.log("datosEnvio: " + datosEnvio)

         mostrarResumen(datosEnvio);
    }
    // Ocultar la sección actual
    if(currentStepElement){
        currentStepElement.classList.remove('active');
        currentStepElement.classList.add('hidden');
    }
    if(currentButonElement){
        currentButonElement.classList.add('hidden');
    }

    // Mostrar la siguiente sección
    if(stepToShowElement){
        stepToShowElement.classList.remove('hidden');
    }

    if(buttonToShowElement){
        buttonToShowElement.classList.remove('hidden');
    }

    // Usar un pequeño retraso para permitir que el navegador procese la visibilidad antes de aplicar la transición
    setTimeout(() => {
        stepToShowElement.classList.add('active');
        if(pickupDetailElement){
            pickupDetailElement.classList.add('active');
        }
    }, 10);
}

document.getElementById("btnStep1").addEventListener("click", function() {
    showStep("step2", "step1", "btnStep1", "btnStep2");
});

// Evento para el botón de paso 2
document.getElementById("btnStep2").addEventListener("click", function() {
    showStep("step3", "step2", "btnStep2", "wallet_container");
});

document.getElementById("local").addEventListener("change", function() {
    if (this.checked) {
       showPickupDetails();
    }
});

document.getElementById("casa").addEventListener("change", function() {
    if (this.checked) {
       showDeliveryDetails();
    }
});

/* ################### Mapa de google para el envio */
function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -27.451, lng: -58.986},
        zoom: 13,
        restriction: {
            latLngBounds: {
                north: -27.4,
                south: -27.5,
                east: -58.95,
                west: -59.02
            },
            strictBounds: true
        }
    });
    var marker = new google.maps.Marker({
        map: map,
        draggable: true,
        position: {lat: -27.451, lng: -58.986}
    });

    marker.addListener('position_changed', function() {
        var pos = marker.getPosition();
        console.log('Selected location: ', pos.lat(), pos.lng());
    });
}

document.addEventListener('DOMContentLoaded', (event) => {
    initMap();
});

/*########################################## Guardar pasos del carrito*/

let retiroLocal = true;
let datosEnvio;
// Almacenar el tipo de envío seleccionado
document.getElementById("local").addEventListener("change", function() {
    if (this.checked) {
        retiroLocal = true;
        datosEnvio = {};  // Limpiar los datos del envío a domicilio
        console.log("Método de entrega seleccionado: Retiro en Local");
    }
});

document.getElementById("casa").addEventListener("change", function() {
    if (this.checked) {
        retiroLocal = false;
        console.log("Método de entrega seleccionado: Envío a Domicilio");
    }
});

function cargarFormEnvio(){
    console. log("por cargar los datos")
    let isMorningChecked = document.getElementById('flexRadioDefault1').checked;
    let isAfternoonChecked = document.getElementById('flexRadioDefault2').checked;
    console.log("isMorningChecked: " + isMorningChecked);
    console.log("isAfternoonChecked: " + isAfternoonChecked);

    let isMorningSelected = isMorningChecked;

    let datos = {
        nombre: document.getElementById("inputNombre").value,
        apellido: document.getElementById("inputApellido").value,
        email: document.getElementById("inputEmail").value,
        telefono: document.getElementById("inputTelefono").value,
        calle: document.getElementById("inputCalle").value,
        altura: document.getElementById("inputAltura").value,
        barrio: document.getElementById("inputBarrio").value,
        casa: document.getElementById("inputCasa").value,
        departamento: document.getElementById("inputDepartamento").value,
        piso: document.getElementById("inputPiso").value,
        entreCalles: document.getElementById("inputEntreCalle").value,
        rangoEntrega: isMorningSelected ? "08hs - 12hs" : "17hs - 20hs",
        descripcion: document.getElementById("inputDescripcion").value
    };
    console.log("guardando datos de envio")
    sessionStorage.setItem('datosEnvio', JSON.stringify(datos));

    return datos;
}

// Mostrar el resumen en el paso 3
function mostrarResumen(datosEnvio) {
    console.log("1: "+datosEnvio)

    const reviewDetails = document.getElementById("review-details");

    // Obtener los datos del carrito desde sessionStorage
    const cartItems = JSON.parse(sessionStorage.getItem("cart")) || [];

    let resumenCarrito = "<h4>Carrito de Compras</h4><ul>";
    cartItems.forEach(item => {
        resumenCarrito += `<li>${item.name} x ${item.quantity} - $${item.price * item.quantity}</li>`;
    });
    resumenCarrito += "</ul>";
    const envioSeleccionado = retiroLocal ? "Retiro en Local" : "Envio a Domicilio"
    // Añadir el método de envío
    let resumenEnvio = `<h4>Método de Envío: ${envioSeleccionado}</h4>`;
    if(datosEnvio){
        console.log("2: "+datosEnvio)


        const infoCasa = datosEnvio.casa != null && datosEnvio.casa != '' ? `<p><strong>Casa:</strong> <span id="casa">${datosEnvio.casa}</span>` : '';
        const infoDpto = datosEnvio.departamento != null && datosEnvio.departamento != '' ? `<strong>Departamento:</strong> <span id="departamento">${datosEnvio.departamento}</span>` : '';
        const infoPiso = datosEnvio.piso != null && datosEnvio.piso != '' ? `<strong>Piso:</strong> <span id="piso">${datosEnvio.piso}</span></p>`: '</p>';
        const infoEntreCalle = datosEnvio.entreCalles != null && datosEnvio.entreCalles != '' ? `<p><strong>Entre calles:</strong> <span id="entreCalles">${datosEnvio.entreCalles}</span></p>`: '';
        const infoDescripcion = datosEnvio.descripcion != null && datosEnvio.descripcion != '' ? `<p><strong>Descripción:</strong> <span id="descripcion">${datosEnvio.descripcion}</span></p>`: '';
        const infoBarrio = datosEnvio.barrio != null && datosEnvio.barrio != '' ?  `<p><strong>Barrio:</strong> <span id="barrio">${datosEnvio.barrio}</span></p>` : '';

        // Si es envío a domicilio, mostrar los detalles
        //        if (!retiroLocal) {
        resumenEnvio += `
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <p><strong>Nombre:</strong> <span id="nombreCompleto">${datosEnvio.nombre} ${datosEnvio.apellido}</span></p>
                        <p><strong>Email:</strong> <span id="email">${datosEnvio.email}</span></p>
                    </div>
                    <div class="col-12">
                        <p><strong>Teléfono:</strong> <span id="telefono">${datosEnvio.telefono}</span>
                        <strong>Dirección:</strong> <span id="direccion">${datosEnvio.calle} ${datosEnvio.altura} ${infoBarrio}</span></p>
                    </div>
                    <div class="col-12" id="casa-depto-piso">
                        ${infoCasa}
                        ${infoDpto}
                        ${infoPiso}
                    </div>
                    <div class="col-12" id="entreCalleId">
                        ${infoEntreCalle}
                    </div>
                    <div class="col-12">
                        <p><strong>Horario de entrega:</strong> <span id="rangoEntrega">${datosEnvio.rangoEntrega}</span></p>
                    </div>
                    <div class="col-12">
                        ${infoDescripcion}
                    </div>
                </div>
            </div>
        `;
    }else {
       resumenEnvio += `
         <div class="container">
             <div class="row">
                 <h6 class="card-subtitle mb-2 text-body-secondary">Datos del local</h6>
                 <div class="col-12">
                     <p><strong>Direccion: </strong> <span id="nombreCompleto">Calle Falsa 1234</span></p>
                 </div>
                 <hr class="custom-hr">
                <h6 class="card-subtitle mb-2 text-body-secondary">Datos de contacto</h6>
                 <div class="col-12">
                     <p><strong>Email de la tienda: </strong> <span id="email">tienda@tienda.com</span></p>
                 </div>
                 <div class="col-12">
                     <p><strong>Teléfono de la tienda: </strong> <span id="telefono">(362) 4-203-698</span></p>
                 </div>
                  <hr class="custom-hr">
                 <p class="col-12" style="margin: 8px 0px;" class="card-text"><small class="text-body-secondary">Recuerde que ante cualquier duda puede comunicarse con nostros.</small></p>
             </div>
         </div>
     `;
    }
    reviewDetails.innerHTML = resumenCarrito + resumenEnvio;

    if(datosEnvio){

        let contenedor = document.querySelector('#casa-depto-piso');
        let pElemento = contenedor.querySelector('p');

        // Verifica si el elemento tiene algún contenido
        if (pElemento) {
            // Si no hay contenido, oculta el elemento
            contenedor.removeChild(pElemento);
        }

        if (!contenedor.innerHTML.trim()) {
            // Si no hay contenido, oculta el elemento
            contenedor.style.display = 'none';
        }

        let entreCalle = document.querySelector('#entreCalleId');
        if (!entreCalle.innerHTML.trim()) {
            // Si no hay contenido, oculta el elemento
            entreCalle.style.display = 'none';
        }
    }
}

function validarFormulario() {
    console.log("aca")
    let nombre = document.getElementById("inputNombre").value.trim();
    let apellido = document.getElementById("inputApellido").value.trim();
    let email = document.getElementById("inputEmail").value.trim();
    let telefono = document.getElementById("inputTelefono").value.trim();
    let calle = document.getElementById("inputCalle").value.trim();
    let altura = document.getElementById("inputAltura").value.trim();
    let rangoEntregaChecked = document.querySelector('input[name="flexRadioDefault"][id="flexRadioDefault1"]:checked');

    let valid;
    // Verificar que los campos requeridos no estén vacíos
    if (!apellido) {
       let alert = document.getElementById("alertApellido");
       alert.classList.remove('hidden');
       alert.textContent="El apellido no puede estar vacio."
       valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertApellido");
        alert.classList.add('hidden');
    }
    console.log("apellido: " + apellido)

    if (!nombre) {
        let alert = document.getElementById("alertNombre");
        alert.classList.remove('hidden');
        alert.textContent="El nombre no puede estar vacio."
        valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertNombre");
        alert.classList.add('hidden');
    }
    console.log("nombre: " + nombre)

    if (!email) {
        let alert = document.getElementById("alertEmail");
        alert.classList.remove('hidden');
        alert.textContent="El nombre no puede estar vacio."
        valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertEmail");
        alert.classList.add('hidden');
    }
    console.log("email: " + email)

    if (!telefono) {
        let alert = document.getElementById("alertTelefono");
        alert.classList.remove('hidden');
        alert.textContent="El nombre no puede estar vacio."
        valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertTelefono");
        alert.classList.add('hidden');
    }
    console.log("telefono: " + telefono)

    if (!calle) {
        let alert = document.getElementById("alertCalle");
        alert.classList.remove('hidden');
        alert.textContent="El nombre no puede estar vacio."
        valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertCalle");
        alert.classList.add('hidden');
    }
    console.log("calle: " + calle)

    if (!altura) {
        let alert = document.getElementById("alertAltura");
        alert.classList.remove('hidden');
        alert.textContent="El nombre no puede estar vacio."
        valid = false; // El formulario no está completo
    }else {
        let alert = document.getElementById("alertAltura");
        alert.classList.add('hidden');
    }
    console.log("altura: " + altura)
    console.log("valid: " + (!nombre && !apellido && !email && !telefono && !calle && !altura))


    return (nombre && apellido && email && telefono && calle && altura); // El formulario está completo
}

function settearForm(){
    console.log("cargadno datos en formulario")
    let envio = JSON.parse(sessionStorage.getItem('datosEnvio')) || [];

    let formNombre = document.getElementById('inputNombre');
    formNombre.value = envio.nombre;

    let formApellido = document.getElementById('inputApellido');
    formApellido.value = envio.apellido;

    let formEmail = document.getElementById('inputEmail');
    formEmail.value = envio.email;

    let formTelefono = document.getElementById('inputTelefono');
    formTelefono.value = envio.telefono;

    let formEntreCalle = document.getElementById('inputEntreCalle');
    formEntreCalle.value = envio.entreCalles;

    let formCalle = document.getElementById('inputCalle');
    formCalle.value = envio.calle;

    let formAltura = document.getElementById('inputAltura');
    formAltura.value = envio.altura;

    let formBarrio = document.getElementById('inputBarrio');
    formBarrio.value = envio.barrio;


    let formCasa= document.getElementById('inputCasa');
    formCasa.value = envio.casa;

    let formDepartamento = document.getElementById('inputDepartamento');
    formDepartamento.value = envio.departamento;

    let formPiso = document.getElementById('inputPiso');
    formPiso.value = envio.piso;

    console.log("rangoEntregaChecked 1: " + (envio.rangoEntrega == '8hs a 12hs'))
    let formLocal = document.getElementById('flexRadioDefault1');
    formLocal.checked = (envio.rangoEntrega == '08hs - 12hs');
    if(envio.rangoEntrega == '9hs a 20hs'){
        console.log("eliminando el checked");
        formLocal.removeAttribute('checked');
    }

    console.log("rangoEntregaChecked 2: " + envio.rangoEntrega)
    console.log("rangoEntregaChecked 2: " + (envio.rangoEntrega == '8hs a 12hs'))
    let formEnvio = document.getElementById('flexRadioDefault2');
    formEnvio.checked = (envio.rangoEntrega == '17hs - 20hs');
    if(envio.rangoEntrega == '8hs a 12hs'){
        console.log("eliminando el checked");
        formEnvio.removeAttribute('checked');
    }

    let formDescripcion = document.getElementById('inputDescripcion');
    formDescripcion.value = envio.descripcion;

    console.log("FIN carga DE datos en formulario")

}