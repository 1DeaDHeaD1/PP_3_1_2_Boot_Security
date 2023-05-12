$( document ).ready( function () {
    var url = window.location.href.substr( window.location.href.lastIndexOf( '/' ) );
    $( 'a' ).each( function () {
        console.log(this);
        if( $( this ).attr( 'href' ) === url &&  $( this ).parent( 'div' ).hasClass('navbar-side')) {
            $( this ).parent( 'div' ).addClass( 'active' );
        }
    });
});
