function runMainPage() {
    $(".carousel-item-1").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        items: 1,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="fa fa-angle-left" aria-hidden="true"></i>',
            '<i class="fa fa-angle-right" aria-hidden="true"></i>'
        ]
    });
}

function runCategorySlide() {
    $(".carousel-item-2").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        margin: 30,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="fa fa-angle-left" aria-hidden="true"></i>',
            '<i class="fa fa-angle-right" aria-hidden="true"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            }
        }
    });
}

function runTopSlider() {
    $(".carousel-item-3").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        margin: 30,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="fa fa-angle-left" aria-hidden="true"></i>',
            '<i class="fa fa-angle-right" aria-hidden="true"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            }
        }
    });
}

function latestSlider() {
    $(".tranding-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        items: 1,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="fa fa-angle-left"></i>',
            '<i class="fa fa-angle-right"></i>'
        ]
    });
}


//mainpage
$.ajax({
    type : 'GET',
    url: '/api/news/search/main-page',
    success: function(response) {
        for (let i = response.length - 1; i >= 0; i--) {
            let element = $(`<div id="main-page-${i}" class="position-relative overflow-hidden" style="height: 435px;">
                            <img class="img-fluid h-100" src="${response[i].imgUrl}" style="object-fit: cover;">
                            <div class="overlay">
                                <div class="mb-1">
                                    <a class="text-white" href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                    <span class="px-2 text-white">/</span>
                                    <span class="text-white" href="">${response[i].updatedDateManual}</span>
                                </div>
                                <a class="h2 m-0 text-white font-weight-bold" href="/news/${response[i].id}">${response[i].title}</a>
                            </div>
                        </div>`);
            $('#main-page-slide').prepend(element);
        }
        runMainPage();
    }
});

// category
$.ajax({
    type: 'GET',
    url: '/api/news/search/category?cateType=all',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let element = `<div class="position-relative">
                            <div id="cate-img-div" style="height: 200px">
                                <img class="img-fluid w-100" src="${response[i].imgUrl}" style="object-fit: cover;">
                            </div>
                            <div class="overlay position-relative bg-light">
                                <div class="mb-2" style="font-size: 13px;">
                                    <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                    <span class="px-1">/</span>
                                    <span>${response[i].updatedDateManual}</span>
                                </div>
                                <a class="h4 m-0" href="/news/${response[i].id}">${response[i].title}</a>
                            </div>
                        </div>`
            if (response[i].category === 'Business') {
                $('#cate-slide-business').prepend(element);
            }
            else if (response[i].category === 'Technology') {
                $('#cate-slide-technology').prepend(element);
            }
            else if (response[i].category === 'Entertainment') {
                $('#cate-slide-entertainment').prepend(element);
            }
            else if (response[i].category === 'Sport') {
                $('#cate-slide-sport').prepend(element);
            }
        }
        let css = `<style>
                        @media only screen and (max-width: 770px) {
                            #cate-img-div {
                                height: auto !important; 
                                max-height: 400px !important;
                            }
                        }
                    </style>`;
        $('head').append(css);
        runCategorySlide();
    }
});


//popular
$.ajax({
    type: 'GET',
    url: '/api/news/search/popular?limit=6',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            if (i <= 1 ) {
                let element = `<img class="img-fluid w-100" src="${response[i].imgUrl}" style="object-fit: cover; max-height: 350px">
                                <div class="overlay position-relative bg-light">
                                    <div class="mb-2" style="font-size: 14px;">
                                        <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                        <span class="px-1">/</span>
                                        <span>${response[i].updatedDateManual}</span>
                                    </div>
                                    <a class="h4" href="/news/${response[i].id}">${response[i].title}</a>
                                    <p class="m-0 text-truncate"
                                        style="display:-webkit-box;
                                        -webkit-line-clamp: 2;
                                        -webkit-box-orient: vertical; 
                                        white-space: normal">${response[i].content}</p>
                                </div>`;
                $(`#popular-${i+1}`).append(element);
            } else {
                let element = `<img src="${response[i].imgUrl}" style="width: 100px; height: 100px; object-fit: cover;">
                                <div class="w-100 d-flex flex-column justify-content-center bg-light px-3" style="height: 100px;">
                                    <div class="mb-1" style="font-size: 13px;">
                                        <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                        <span class="px-1">/</span>
                                        <span>${response[i].updatedDateManual}</span>
                                    </div>
                                    <a class="h6 m-0" href="/news/${response[i].id}">${response[i].title}</a>
                                </div>`
                $(`#popular-${i+1}`).append(element);
            }
        }
    }
});

//latest
$.ajax({
    type: 'GET',
    url: '/api/news/search/latest?limit=6',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let topLatestElement = `<div class="text-truncate"><a class="text-secondary" href="/news/${response[i].id}">${response[i].title}</a></div>`
            $('#top-latest-slider').prepend(topLatestElement);
            if (i <= 1 ) {
                let element = `<img class="img-fluid w-100" src="${response[i].imgUrl}" style="object-fit: cover; max-height: 350px">
                                <div class="overlay position-relative bg-light">
                                    <div class="mb-2" style="font-size: 14px;">
                                        <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                        <span class="px-1">/</span>
                                        <span>${response[i].updatedDateManual}</span>
                                    </div>
                                    <a class="h4" href="/news/${response[i].id}">${response[i].title}</a>
                                    <p class="m-0 text-truncate"
                                        style="display:-webkit-box;
                                        -webkit-line-clamp: 2;
                                        -webkit-box-orient: vertical; 
                                        white-space: normal">${response[i].content}</p>
                                </div>`;
                $(`#latest-${i+1}`).append(element);
            } else {
                let element = `<img src="${response[i].imgUrl}" style="width: 100px; height: 100px; object-fit: cover;">
                                <div class="w-100 d-flex flex-column justify-content-center bg-light px-3" style="height: 100px;">
                                    <div class="mb-1" style="font-size: 13px;">
                                        <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                        <span class="px-1">/</span>
                                        <span>${response[i].updatedDateManual}</span>
                                    </div>
                                    <a class="h6 m-0" href="/news/${response[i].id}">${response[i].title}</a>
                                </div>`
                $(`#latest-${i+1}`).append(element);
            }
        }
        latestSlider();
    }
});

//trending and top slider
$.ajax({
    type: 'GET',
    url: '/api/news/search/trending?limit=5',
    success: function(response) {
        for (let i = 0; i < response.length; i++) {
            let element = `<div class="d-flex mb-3">
                            <img src="${response[i].imgUrl}" style="width: 100px; height: 100px; object-fit: cover;">
                            <div class="w-100 d-flex flex-column justify-content-center bg-light px-3" style="height: 100px;">
                                <div class="mb-1" style="font-size: 13px;">
                                    <a href="/news/search/category?cateType=${response[i].category}">${response[i].category}</a>
                                    <span class="px-1">/</span>
                                    <span>${response[i].updatedDateManual}</span>
                                </div>
                                <a class="h6 m-0" href="/news/${response[i].id}">${response[i].title}</a>
                            </div>
                        </div>`
            $('#trending').append(element);
            if (i < 4) {
                let topElement = `<div class="trending-top-slider d-flex" onclick="trendingTopSlider(${response[i].id})" style="background-color: #ffffff">
                    <img src="${response[i].imgUrl}" style="width: 80px; height: 80px; object-fit: cover;">
                    <div class="d-flex align-items-center bg-light px-3" style="height: 80px;">
                        <a class="text-secondary font-weight-semi-bold" href="/news/${response[i].id}">${response[i].title}</a>
                    </div>
                </div>`
                $('#top-slider').prepend(topElement);
            }
        }
        runTopSlider();
    }
});

//trending-top-slider click
function trendingTopSlider(id) {
    window.location.href = '/news/' + id;
}

//tags
$.ajax({
    type: 'GET',
    url: '/api/tag?limit=12',
    success: function (response) {
        response.forEach(function (tag) {
            $('.fragment-tags').append(`<a href="/news/search/tag?tag=${tag}" class="btn btn-sm btn-outline-secondary m-1">${'#' + tag}</a>`)
        })
    }
})

//today
const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
const today = new Date();
const day = days[today.getDay()];
const date = today.getDate()
const month = months[today.getMonth()];
const year = today.getFullYear();

$('#today').text(day + ', ' + month + ' ' + date + ', ' + year);