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
    url: '/news/search/trending?limit=5',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let element = `<div class="d-flex mb-3">
                            <img src="${response[i].imgUrl}" style="width: 100px; height: 100px; object-fit: cover;">
                            <div class="w-100 d-flex flex-column justify-content-center bg-light px-3" style="height: 100px;">
                                <div class="mb-1" style="font-size: 13px;">
                                    <a href="">${response[i].category}</a>
                                    <span class="px-1">/</span>
                                    <span>${response[i].updatedDateManual}</span>
                                </div>
                                <a class="h6 m-0" href="">${response[i].title}</a>
                            </div>
                        </div>`
            $('#trending').append(element);
            if (i < 4) {
                let topElement = `<div class="d-flex" style="background-color: #ffffff">
                    <img src="${response[i].imgUrl}" style="width: 80px; height: 80px; object-fit: cover;">
                    <div class="d-flex align-items-center bg-light px-3" style="height: 80px;">
                        <a class="text-secondary font-weight-semi-bold" href="">${response[i].title}</a>
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
    url: '/news/search/latest?limit=6',
    success: function (response) {
        for (let i = 0; i < response.length; i++) {
            let topLatestElement = `<div class="text-truncate"><a class="text-secondary" href="">${response[i].title}</a></div>`
            $('#top-latest-slider').prepend(topLatestElement);
        }
        latestSlider();
    }
});