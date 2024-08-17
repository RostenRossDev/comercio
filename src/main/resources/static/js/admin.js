document.addEventListener('DOMContentLoaded', () => {
    loadProductsFromSessionStorage();
});

//function filterProducts() {
//    const formData = new FormData(document.getElementById('filterForm'));
//    const filterData = Object.fromEntries(formData.entries());
//
//    fetch('/administracion-negocio/filter', {
//        method: 'POST',
//        headers: {
//            'Content-Type': 'application/json',
//        },
//        body: JSON.stringify(filterData),
//    })
//    .then(response => response.json())
//    .then(data => {
//        sessionStorage.setItem('products', JSON.stringify(data));
//        updateProductTable(data);
//    });
//}

document.addEventListener('DOMContentLoaded', function () {
    fetch('/material') // Ajusta la URL a la correcta
        .then(response => response.json())
        .then(data => {
            const materialSelect = document.getElementById('materialAdmin');
            materialSelect.innerHTML = ''; // Limpia el select
            data.forEach(material => {
                const option = document.createElement('option');
                option.value = material.name; // Suponiendo que el objeto tiene una propiedad 'value'
                option.textContent = material.name; // Suponiendo que el objeto tiene una propiedad 'label'
                materialSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar materiales:', error));
});


// session storage cuando cambia los inputs

function saveToSessionStorage() {
    const name = document.getElementById('nameAdmin').value;
    const material = document.getElementById('materialAdmin').value;
    const price = document.getElementById('priceAdmin').value;
    const discount = document.getElementById('discountAdmin').value;
    const tags = document.getElementById('tagsAdmin').value;

    const filters = {
        name,
        material,
        price,
        discount,
        tags
    };

    sessionStorage.setItem('filters', JSON.stringify(filters));
    console.log("filters: " + JSON.stringify(filters))
    // Enviar la información de los filtros al backend
    fetch(`/filtrar?name=${filters.name}&material=${filters.material}&price=${filters.price}&discount=${filters.discount}&tags=${filters.tags}`)
    .then(response => response.json())
    .then(data => {
        // Manejar la respuesta del backend
        console.log('Filtrado:', JSON.stringify(data));
        fillProdcuts(data);
    })
    .catch(error => console.error('Error al filtrar:', error));
}

// Agregar event listeners a los inputs
document.getElementById('nameAdmin').addEventListener('input', saveToSessionStorage);
document.getElementById('materialAdmin').addEventListener('change', saveToSessionStorage);
document.getElementById('priceAdmin').addEventListener('input', saveToSessionStorage);
document.getElementById('discountAdmin').addEventListener('input', saveToSessionStorage);
document.getElementById('tagsAdmin').addEventListener('input', saveToSessionStorage);

function fillProdcuts(data){


  const container = document.getElementById('tbody');
    container.innerHTML = ''; // Limpia el contenedor antes de agregar nuevos productos

    // Itera sobre los productos y crea el HTML para cada uno
    data.forEach(product => {
            const tagsArray = product.tag ? product.tag.split(' ') : [];
            // Une las palabras del array con comas
            const tagsFormatted = tagsArray.join(', ');
            const row = document.createElement('tr');

            const productHTML = `
            <tr>
                <td><img src="/img/${product.img}" alt="${product.name}" /></td>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td>${product.sale}</td>
                <td>${product.active ? 'Sí' : 'No'}</td>
                <td>${tagsFormatted}</td> <!-- Muestra las etiquetas separadas por comas -->
                <td>${product.stock}</td>
                <td>${product.material}</td>
                <td>
                   <button onclick="editProduct(${product.id})">Editar</button>
                   <button onclick="deleteProduct(${product.id})">Borrar</button>
                </td>
            </tr>

        `;

        // Inserta el HTML generado en el contenedor
        container.insertAdjacentHTML('beforeend', productHTML);
    });
}

//// relenar inputs

document.addEventListener('DOMContentLoaded', function () {
    // Obtener los valores de sessionStorage
    const storedFilters = JSON.parse(sessionStorage.getItem('filters'));

    if (storedFilters) {
        // Rellenar los inputs con los valores de sessionStorage
        document.getElementById('nameAdmin').value = storedFilters.name || '';
        document.getElementById('priceAdmin').value = storedFilters.price || '';
        document.getElementById('discountAdmin').value = storedFilters.discount || '';
        document.getElementById('tagsAdmin').value = storedFilters.tags || '';
    }
});


//
//function updateProductTable(products) {
//    const tbody = document.querySelector('#productTable tbody');
//    tbody.innerHTML = ''; // Clear existing rows
//    products.forEach(product => {
//        const row = document.createElement('tr');
//        row.id = `product-${product.id}`;
//        row.innerHTML = `
//            <td><img src="${product.image}" alt="${product.name}" /></td>
//            <td>${product.name}</td>
//            <td>${product.price}</td>
//            <td>${product.discount}</td>
//            <td>${product.active ? 'Sí' : 'No'}</td>
//            <td>${product.tags.join(', ')}</td>
//            <td>${product.stock}</td>
//            <td>${product.material}</td>
//            <td>
//                <button onclick="editProduct(${product.id})">Editar</button>
//                <button onclick="deleteProduct(${product.id})">Borrar</button>
//            </td>
//        `;
//        tbody.appendChild(row);
//    });
//}

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