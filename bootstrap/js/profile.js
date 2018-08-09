// JS for Category dropdown menu
function myFunction() {
    var x = document.getElementById("Demo");
    if (x.className.indexOf("w3-show") == -1) {
      x.className += " w3-show";
    } else {
      x.className = x.className.replace(" w3-show", "");
    }
  }
  
  // JS back to top button
  window.onscroll = function() {scrollFunction()};
  function scrollFunction() {
      if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
          document.getElementById("myBtn").style.display = "block";
      } else {
          document.getElementById("myBtn").style.display = "none";
      }
  }
  function topFunction() {
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
  }

// Source: http://jsfiddle.net/pbzt812g/778/
// Create a flexible right side if page based on sidebar menu
  $(document).ready(function () {
    $('.user-menu ul li:first').addClass('active');
    $('.tab-content:not(:first)').hide();
    $('.user-menu ul li a').click(function (event) {
        event.preventDefault();
        var content = $(this).attr('href');
        $(this).parent().addClass('active');
        $(this).parent().siblings().removeClass('active');
        $(content).show();
        $(content).siblings('.tab-content').hide();
    });
});

// Source: https://www.w3schools.com/graphics/tryit.asp?filename=trymap_overlays_infowindow
// Google Map
