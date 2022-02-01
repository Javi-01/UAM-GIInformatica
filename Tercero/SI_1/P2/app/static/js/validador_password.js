$(document).ready(function() {

  $('input[id=contraseña1]').keyup(function() {
      // set password variable
      var pswd = $(this).val();
      //validate the length
      if ( pswd.length < 8 ) {
          $('#length').removeClass('valid').addClass('invalid');
      } else {
          $('#length').removeClass('invalid').addClass('valid');
      }

      //validate letter
      if ( pswd.match(/[A-z]/) ) {
          $('#letter').removeClass('invalid').addClass('valid');
      } else {
          $('#letter').removeClass('valid').addClass('invalid');
      }

      //validate capital letter
      if ( pswd.match(/[A-Z]/) ) {
          $('#capital').removeClass('invalid').addClass('valid');
      } else {
          $('#capital').removeClass('valid').addClass('invalid');
      }

      //validate number
      if ( pswd.match(/\d/) ) {
          $('#number').removeClass('invalid').addClass('valid');
      } else {
          $('#number').removeClass('valid').addClass('invalid');
      }

  }).focus(function() {
      $('#pswd-info').show();
      $('#length').addClass('invalid');
      $('#letter').addClass('invalid');
      $('#capital').addClass('invalid');
      $('#number').addClass('invalid');
  }).blur(function() {
      $('#pswd-info').hide();
  });

});

$(document).ready(function() {

    $('input[id=contraseña]').keyup(function() {
        // set password variable
        var pswd = $(this).val();
        //validate the length
        if ( pswd.length < 8 ) {
            $('#length').removeClass('valid').addClass('invalid');
        } else {
            $('#length').removeClass('invalid').addClass('valid');
        }
  
        //validate letter
        if ( pswd.match(/[A-z]/) ) {
            $('#letter').removeClass('invalid').addClass('valid');
        } else {
            $('#letter').removeClass('valid').addClass('invalid');
        }
  
        //validate capital letter
        if ( pswd.match(/[A-Z]/) ) {
            $('#capital').removeClass('invalid').addClass('valid');
        } else {
            $('#capital').removeClass('valid').addClass('invalid');
        }
  
        //validate number
        if ( pswd.match(/\d/) ) {
            $('#number').removeClass('invalid').addClass('valid');
        } else {
            $('#number').removeClass('valid').addClass('invalid');
        }
  
    }).focus(function() {
        $('#pswd-info').show();
        $('#length').addClass('invalid');
        $('#letter').addClass('invalid');
        $('#capital').addClass('invalid');
        $('#number').addClass('invalid');
    }).blur(function() {
        $('#pswd-info').hide();
    });
  
  });