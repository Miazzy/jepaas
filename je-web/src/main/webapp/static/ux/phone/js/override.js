var __ajax__ = JE.ajax;
function getCookieValue(name){
    var cookies = document.cookie.split('; '),
        i = cookies.length,
        cookie, value;

    while(i--) {
       cookie = cookies[i].split('=');
       if (cookie[0] === name) {
           value = cookie[1];
       }
    }

    return value;
}
JE.ajax = function(config){
    if(config.url.indexOf('/') == 0){
        config.url = config.url.substring(1);
    }
    var url = JE.getUrlMaps(config.url);
    if(!url){
        url = JE.replaceOldUrl(config.url);
    }
    config.url = url || config.url;
    config.headers = {authorization:getCookieValue('authorization')};
    return __ajax__(config);
};
