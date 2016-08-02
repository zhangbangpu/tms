var appjsFiles = [
    window.location.origin + '/js/ips.public.js',
    window.location.origin + '/js/demo.js',
    window.location.origin + '/js/app.js'
];


for (var index in appjsFiles) {
    loadFile(appjsFiles[index]);
}


