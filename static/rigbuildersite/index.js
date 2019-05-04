var PartsData;
var TotalPrice=0;
var DisableRecalculateTotalPrice=false;

function InitPage() {
    var jqxhr = $.ajax( "data" )
    .done(Setup)
    .fail(function() {
        alert( "error" );
    });
}

function Setup(data) {
    PartsData = JSON.parse(data);
    $(".select-part").on("change", function() {
        var partId = parseInt($(this).val());
        var partData = PartsData['parts'][partId];

        var extras = partData["description"];
        if (extras.length > 0) extras += " | ";

        if (partData["product-page"].length > 0) {
            var link = $("<a>").attr("href", partData["product-page"]).attr("target", "_blank").text("Product Page");
            extras += link.prop("outerHTML");
        }
        if (extras.length > 0) extras = " | " + extras;
        $("."+partData["part-type"]+"-extra").html(extras);

        if (partData['make-thumbnail'].length > 0) {
            $("."+partData["part-type"]+"-card-background").css("background-image", "url("+partData['make-thumbnail']+"), linear-gradient(rgba(255,255,255,0.5),rgba(255,255,255,0.5))");
        } else {
            $("."+partData["part-type"]+"-card-background").css("background-image", "");
        }
        if (!DisableRecalculateTotalPrice) CalculateTotalPrice();
    });

    $(".review-div-title").click(ToggleReviewDivMask);
    $(".review-div-mask").click(ToggleReviewDivMask);
    $("#btn-copy-clipboard").click(CopyToClipboard);
}

function CalculateTotalPrice() {
    OldTotalPrice = TotalPrice;
    TotalPrice = 0;
    $(".select-part").each(function() {
        var partId = parseInt($(this).val());
        var partData = PartsData['parts'][partId];
        TotalPrice += parseInt(partData['price']);
    });

    $(".total-price-value")
        .prop('number', OldTotalPrice)
        .animateNumber({number:TotalPrice}, {easing: 'swing',duration: 300});
}

function ToggleReviewDivMask() {
    if ($(".review-div-mask").css("visibility") == "visible") {
         $(".review-div-details-frame").animate({height: "0px"}, 300, function() {
            $(".review-div-details-frame").css("display", "none");
        });
        $(".review-div-mask").animate({opacity: 0.0}, 300, function() {
            $(".review-div-mask").css("visibility", "hidden");
        });
        $('body').css({
            overflow: 'auto',
            height: 'auto'
        });
    } else {
        $(".review-div-details").html(GenerateReviewTable());
        $(".review-div-details-frame").css("display", "initial");
        $(".review-div-mask").css("visibility", "visible");
        $(".review-div-mask").animate({opacity: 0.7}, 300);
        $(".review-div-details-frame").animate({height: "300px"}, 300);
        $('body').css({
            overflow: 'hidden',
            height: '100%'
        });
    }
}

function GenerateReviewTable() {
     var htmlCode = "<table>";
     $(".select-part").each(function() {
        var partId = parseInt($(this).val());
        var partData = PartsData['parts'][partId];
        if (parseInt(partData['price']) > 0) {
            htmlCode += "<tr>";
            htmlCode += "<td>"+partData['name']+"</td>";
            htmlCode += "<td>RM"+partData['price']+"</td>";
            htmlCode += "</tr>";
        }
    });
    htmlCode += "</table>";
    return htmlCode;
}

function CopyToClipboard() {
     var result = "";
     $(".select-part").each(function() {
        var partId = parseInt($(this).val());
        var partData = PartsData['parts'][partId];
        if (parseInt(partData['price']) > 0) {
            result += partData['name']+" RM"+partData['price']+"\n";
        }
    });
    result += "\nTotal : RM"+TotalPrice;
    $("#review-div-details-clipboard").val(result);
    $("#review-div-details-clipboard").css("display", "block");
    $("#review-div-details-clipboard").select();
    setTimeout(function() {
        $("#review-div-details-clipboard").css("display", "none");
    },1);
    document.execCommand("copy");
    M.toast({html: 'List copied to clipboard!'})
}

function setBuild(buildID) {
    var instance = M.Sidenav.getInstance($(".sidenav"));
    instance.close();

    buildData = PartsData['builds'][parseInt(buildID)];
    DisableRecalculateTotalPrice = true;
    $(".select-part").each(function() {
        var slug = $(this).attr('part');
        $(this).val(PartsData['na_parts'][slug]);
        M.FormSelect.init($(this),{});
        $(this).change();
    })

    for (var key in buildData) {
        var selectKey = "#select-part-"+key;
        $(selectKey).val(buildData[key]);
        M.FormSelect.init($(selectKey),{});
        $(selectKey).change();
    }
    DisableRecalculateTotalPrice = false;

    CalculateTotalPrice();
}