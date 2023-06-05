function runTopSlider() {
    $(".carousel-item-3").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        margin: 30,
        dots: false,
        loop: true,
        nav: true,
        navText: [
            '<i class="fa fa-angle-left" aria-hidden="true"></i>',
            '<i class="fa fa-angle-right" aria-hidden="true"></i>'
        ],
        responsive: {
            0: {
                items: 1
            },
            576: {
                items: 1
            },
            768: {
                items: 2
            },
            992: {
                items: 3
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
        nav: true,
        navText: [
            '<i class="fa fa-angle-left"></i>',
            '<i class="fa fa-angle-right"></i>'
        ]
    });
}

//trending and top slider
$.ajax({
    type: 'GET',
    url: '/api/news/search/trending?limit=5',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let element = `<div class="d-flex mb-3">
                            <img src="${response[i].imgUrl}" style="width: 100px; height: 100px; object-fit: cover;">
                            <div class="w-100 d-flex flex-column justify-content-center bg-light px-3" style="height: 100px;">
                                <div class="mb-1" style="font-size: 13px;">
                                    <a href="/news/search/${response[i].category}">${response[i].category}</a>
                                    <span class="px-1">/</span>
                                    <span>${response[i].updatedDateManual}</span>
                                </div>
                                <a class="h6 m-0" href="/news/${response[i].id}">${response[i].title}</a>
                            </div>
                        </div>`
            $('#trending').append(element);
            if (i < 4) {
                let topElement = `<div class="d-flex" style="background-color: #ffffff">
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

$.ajax({
    type: 'GET',
    url: '/api/news/search/latest?limit=6',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let topLatestElement = `<div class="text-truncate"><a class="text-secondary" href="/news/${response[i].id}">${response[i].title}</a></div>`
            $('#top-latest-slider').prepend(topLatestElement);
        }
        latestSlider();
    }
});

//nav active
$('#nav-items a').removeClass('active');

const pathName = $(location).attr('pathname');
const pathNameSplit = pathName.split("/");
let searchType = '';

if (pathNameSplit.includes('search')) {
    searchType = pathNameSplit[pathNameSplit.length - 1];
}

let searchParams = new URLSearchParams(window.location.search);
let cateType = searchParams.get('cateType');

if (pathNameSplit.includes('contact')) {
    $('#nav-contact').addClass('active');
} else if (searchType !== '') {
    if (cateType != null && cateType !== '') {
        $(`#nav-${cateType.toLowerCase()}`).addClass('active');
    } else {
        $(`#nav-${searchType.toLowerCase()}`).addClass('active');
    }
} else {
    $('#nav-home').addClass('active');
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