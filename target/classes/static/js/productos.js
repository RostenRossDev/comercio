document.addEventListener('DOMContentLoaded', function () {
    fetch('/material') // Ajusta la URL a la correcta
        .then(response => response.json())
        .then(data => {
            const materialSelect = document.getElementById('material');
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
    const name = document.getElementById('name').value;
    const material = document.getElementById('material').value;
    const price = document.getElementById('price').value;
    const discount = document.getElementById('discount').value;
    const tags = document.getElementById('tags').value;

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
document.getElementById('name').addEventListener('input', saveToSessionStorage);
document.getElementById('material').addEventListener('change', saveToSessionStorage);
document.getElementById('price').addEventListener('input', saveToSessionStorage);
document.getElementById('discount').addEventListener('input', saveToSessionStorage);
document.getElementById('tags').addEventListener('input', saveToSessionStorage);

function fillProdcuts(data){
  const container = document.getElementById('product-container');
    container.innerHTML = ''; // Limpia el contenedor antes de agregar nuevos productos

    // Itera sobre los productos y crea el HTML para cada uno
    data.forEach(product => {
        const productHTML = `
            <div class="col-md-3 mb-3">
                <div class="carousel-content">
                    <div class="image-container">
                        <img src="/img/${product.img}" alt="${product.name}">
                        <div class="zoom-icon">
                            <i class="bi bi-search"></i>
                        </div>
                    </div>
                    <div class="product-info">
                        <p class="product-name">${product.name}</p>
                        <div class="price-discount-container">
                            <button class="btn-price btn-custom" onclick="addToCart('${product.name}', '${product.id}', ${product.price}, ${product.sale})">
                                $${product.price.toFixed(2)}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
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
        document.getElementById('name').value = storedFilters.name || '';
        document.getElementById('price').value = storedFilters.price || '';
        document.getElementById('discount').value = storedFilters.discount || '';
        document.getElementById('tags').value = storedFilters.tags || '';
    }
});
