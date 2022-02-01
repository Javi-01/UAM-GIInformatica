const form = document.getElementById('formulario')

const username = document.getElementById('usuario')
const password1 = document.getElementById('contraseña1')
const password2 = document.getElementById('contraseña2')
const email = document.getElementById('correo')
const address = document.getElementById('direccion')
const card = document.getElementById('tarjeta')
noti = document.getElementById('cerrarNoti')

const usuarioValid = document.getElementById('user-valid')

const expresiones = {
	usuario: /^[a-zA-Z0-9]{6,12}$/, // Letras y numeros
	contraseña: /^.{8,50}$/, // 4 a 12 digitos.
	correo: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
	tarjeta: /^\d{16}$/, // 16 digitos
    direccion: /^[a-zA-Z0-9]{5,50}$/, // direccion de 10 a 50 caracteres.
}

//Validar Formulario
if (form){
    form.addEventListener('submit', (e)=> {
    
        if (validarUsuario() &&
            validarContraseña() &&
            validarCorreo() &&
            validarDireccion() &&
            validarTarjeta()) {
                
            }else{
                e.preventDefault()
            }
    
    })
} 

//Validar Usuario
function validarUsuario() {
    if (validarSiCampoVacio(username)) return false;
    if (!validarExpresionUsuario(username)) return false;
    return true;
}

function validarExpresionUsuario(field) {
    if (expresiones.usuario.test(field.value)) {
        esValido(field);
        return true;
    }else {
        esInvalido(field, `${field.name} debe contener de 6 a 12 letras y numeros`)
        return false;
    }
}

//Validar Email
function validarCorreo() {
    if (validarSiCampoVacio(email)) return false;
    if (!validarExpresionCorreo(email)) return false;
    return true;
}

function validarExpresionCorreo(field) {
    if (expresiones.correo.test(field.value)) {
        esValido(field);
        return true;
    }else {
        esInvalido(field, `${field.name} debe contener el formato correcto de email`)
        return false;
    }
}

//Validar Contraseñas
function validarContraseña() {
    if (validarSiCampoVacio(password1) || validarSiCampoVacio(password2)) return false;
    if (!validarExpresionContraseña(password1) || !validarExpresionContraseña(password2)) return false;
    if (!validarContraseñasIguales(password1, password2)) return;
    return true;
}

function validarExpresionContraseña(field) {
    if (expresiones.contraseña.test(field.value)) {
        esValido(field);
        return true;
    }else {
        esInvalido(field, `${field.name} debe contener al menos 8 caracteres`)
        return false;
    }
}

function validarContraseñasIguales(field1, field2) {
    if (field1.value == field2.value) {
        esValido(field1);
        esValido(field2);
        return true;
    }else {
        esInvalido(field2, `Las contraseñas no coinciden`)
        return false;
    }
}

//Validar Tarjeta
function validarTarjeta() {
    if (validarSiCampoVacio(card)) return;
    if (!validarExpresionTarjeta(card)) return;
    return true;
}

function validarExpresionTarjeta(field) {
    if (expresiones.tarjeta.test(field.value)) {
        esValido(field);
        return true;
    }else {
        esInvalido(field, `${field.name} debe tener 16 digitos`)
        return false;
    }
}

//Validar Direccion
function validarDireccion() {
    if (validarSiCampoVacio(address)) return;
    if (!validarExpresionDireccion(address)) return;
    return true;
}

function validarExpresionDireccion(field) {
    if (expresiones.direccion.test(field.value)) {
        esValido(field);
        return true;
    }else {
        esInvalido(field, `${field.name} debe tener como maximo 50 caracteres`)
        return false;
    }
}

//Funciones auxiliares
function validarSiCampoVacio(field) {
    if (esVacio(field.value.trim())) {
        esInvalido(field, `${field.name} no debe estar vacio`);
        return true;
    }else {
        esValido(field);
        return false;
    }
}

function esVacio(value) {
    if (value === '') return true;
    return false;
}

function esInvalido(field, message) {
    field.classList.remove('valid-form');
    field.classList.add('invalid-form');
    field.nextElementSibling.innerHTML = message;
    field.nextElementSibling.style.color = "red";
}

function esValido(field) {
    field.classList.remove('invalid-form');
    field.classList.add('valid-form');
    field.nextElementSibling.innerHTML = '';
}

if(noti){
    noti.addEventListener('submit', (e)=> {
        click = document.getElementById('alerta')
        click.classList.add("hide")
    })
}
