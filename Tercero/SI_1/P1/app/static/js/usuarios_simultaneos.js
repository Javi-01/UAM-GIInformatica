
$(document).ready(function() {
    function usuarioSimultaneos() {
        $.ajax({
            type: "POST",
            url: "/num_random",
            success: function(response) {
                document.getElementById("info-usuarios").innerHTML = response["num"];
            }
        });
    }
    setInterval(usuarioSimultaneos, 3000); /* Cada 3 sec se retorno un valor aleatrio */
});