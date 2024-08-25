function entregar (itemId , ventaId) {

    fetch(`/entregar?itemId=${itemId}&ventaId=${ventaId}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }
        return response.text(); // o response.json() si esperas una respuesta JSON
    })
    .then(data => {
        console.log('Respuesta:', data);
        window.location.href = '/facturacion';
    })
    .catch(error => {
        console.error('Hubo un problema con la solicitud:', error);
    });
}

function entregarTodo (ventaId) {

    fetch(`/entregarTodo?ventaId=${ventaId}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }
        return response.text(); // o response.json() si esperas una respuesta JSON
    })
    .then(data => {
        console.log('Respuesta:', data);
        window.location.href = '/facturacion';
    })
    .catch(error => {
        console.error('Hubo un problema con la solicitud:', error);
    });
}