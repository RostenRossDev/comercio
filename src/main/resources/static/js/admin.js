document.addEventListener('DOMContentLoaded', () => {
    loadProductsFromSessionStorage();
});

function filterProducts() {
    const formData = new FormData(document.getElementById('filterForm'));
    const filterData = Object.fromEntries(formData.entries());

    fetch('/administracion-negocio/filter', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(filterData),
    })
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('products', JSON.stringify(data));
        updateProductTable(data);
    });
}

function updateProductTable(products) {
    const tbody = document.querySelector('#productTable tbody');
    tbody.innerHTML = ''; // Clear existing rows
    products.forEach(product => {
        const row = document.createElement('tr');
        row.id = `product-${product.id}`;
        row.innerHTML = `
            <td><img src="${product.image}" alt="${product.name}" /></td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.discount}</td>
            <td>${product.active ? 'Sí' : 'No'}</td>
            <td>${product.tags.join(', ')}</td>
            <td>${product.stock}</td>
            <td>${product.material}</td>
            <td>
                <button onclick="editProduct(${product.id})">Editar</button>
                <button onclick="deleteProduct(${product.id})">Borrar</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function editProduct(productId) {
    // Implement your edit logic here
    const row = document.getElementById(`product-${productId}`);
    row.style.backgroundColor = '#f0e68c'; // Change row color to indicate editing
}

function deleteProduct(productId) {
    // Implement your delete logic here
}

function confirmChanges() {
    const updatedProducts = []; // Gather all updated products
    // Implement logic to gather updated products
    fetch('/administracion-negocio/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedProducts),
    })
    .then(response => response.text())
    .then(data => {
        if (data === 'success') {
            alert('Cambios confirmados');
            sessionStorage.removeItem('updatedProducts');
        }
    });
}

function loadProductsFromSessionStorage() {
    const products = JSON.parse(sessionStorage.getItem('products')) || [];
    if (products.length > 0) {
        updateProductTable(products);
    }
}


/* ############################################################## tag filter */

document.addEventListener('DOMContentLoaded', (event) => {
    const tagInput = document.querySelector('.tag-input');
    const tagContainer = document.querySelector('.tag-input-container');

    // Cargar tags desde sessionStorage
    const storedTags = JSON.parse(sessionStorage.getItem('tags')) || [];
    storedTags.forEach(tag => addTag(tag));

    tagInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter' && tagInput.value.trim() !== '') {
            event.preventDefault();
            addTag(tagInput.value.trim());
            tagInput.value = '';
        }
    });

    function addTag(tag) {
        const tagElement = document.createElement('div');
        tagElement.classList.add('tag');
        tagElement.innerHTML = `<span>${tag}</span><span class="remove-tag" onclick="removeTag(this)">x</span>`;
        tagContainer.insertBefore(tagElement, tagInput);

        // Guardar tags en sessionStorage
        const tags = getTags();
        tags.push(tag);
        sessionStorage.setItem('tags', JSON.stringify(tags));
    }

    function removeTag(element) {
        const tagElement = element.parentNode;
        const tagText = tagElement.querySelector('span').innerText;

        tagElement.remove();

        // Actualizar tags en sessionStorage
        const tags = getTags().filter(tag => tag !== tagText);
        sessionStorage.setItem('tags', JSON.stringify(tags));
    }

    function getTags() {
        return JSON.parse(sessionStorage.getItem('tags')) || [];
    }
});

function removeTag(element) {
    const tagElement = element.parentNode;
    const tagText = tagElement.querySelector('span').innerText;

    tagElement.remove();

    // Actualizar tags en sessionStorage
    const tags = JSON.parse(sessionStorage.getItem('tags')) || [];
    const newTags = tags.filter(tag => tag !== tagText);
    sessionStorage.setItem('tags', JSON.stringify(newTags));
}

/*################## modales */

function changeNumber(){
    Swal.fire({
        title: 'Nuevo Numero de Contacto',
        input: 'text',
        inputLabel: 'Numero con caracteristica sin el 15',
        inputPlaceholder: 'Ingrese un nuevo numero',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un numer!'
            }
            if (!/^\d+$/.test(value)) {
                return '¡Por favor, ingresa solo números!';
            }
            if (!/^\d{7}$/.test(value)) {
                return '¡Por favor, ingresa un número de 7 dígitos!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Número de contacto guardado', 'success');
        }
    })
}



function changeEmail(){
Swal.fire({
    title: 'Nuevo Email de Contacto',
    input: 'email',
    inputLabel: 'Email de Contacto',
    inputPlaceholder: 'Ingrese un nuevo email',
    showCancelButton: true,
    inputValidator: (value) => {
        if (!value) {
            return '¡Necesitas ingresar un email!';
        }
    }
}).then((result) => {
    if (result.isConfirmed) {
        Swal.fire('Guardado', 'Email de contacto guardado', 'success');
    }
});
}

function changeSiteName(){
    Swal.fire({
        title: 'Nuevo Nombre del Sitio Web',
        input: 'text',
        inputLabel: 'Nombre del Sitio Web',
        inputPlaceholder: 'Ingrese un nuevo nombre para el sitio web',
        showCancelButton: true,
        inputValidator: (value) => {
            if (!value) {
                return '¡Necesitas ingresar un nombre!';
            }
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Nombre del sitio web guardado', 'success');
        }
    });
}

function changeSocialLinks(){
     Swal.fire({
        title: 'Redes Sociales',
        html:
            '<input id="swal-facebook" class="swal2-input" placeholder="URL de Facebook">' +
            '<input id="swal-twitter" class="swal2-input" placeholder="URL de Twitter">' +
            '<input id="swal-instagram" class="swal2-input" placeholder="URL de Instagram">' +
            '<input id="swal-youtube" class="swal2-input" placeholder="URL de YouTube">',
        showCancelButton: true,
        focusConfirm: false,
        preConfirm: () => {
            const facebook = document.getElementById('swal-facebook').value;
            const twitter = document.getElementById('swal-twitter').value;
            const instagram = document.getElementById('swal-instagram').value;
            const youtube = document.getElementById('swal-youtube').value;

            if (!facebook || !twitter || !instagram || !youtube) {
                Swal.showValidationMessage('¡Todos los campos son obligatorios!');
                return false;
            }

            return {
                facebook,
                twitter,
                instagram,
                youtube
            };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Guardado', 'Redes sociales guardadas', 'success');
        }
    });
}