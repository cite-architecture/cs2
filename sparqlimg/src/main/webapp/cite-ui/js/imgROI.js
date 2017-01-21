  jQuery(function($){

      // Get image-URN from parameters.
      //set a default URN in gradle build.
      var defaultURN, paramURN, imgURN, imgRoi;

	defaultURN = "urn:cite2:hmt:vaimg.v1:VA024RN_0025";
	var imgSvcURL = "@imgapi@?";
	var detailWidth = "400";
	var ictUrl = "ict.html";

	paramURN = get("urn");

	if (paramURN != null){
			imgURN = plainUrn( paramURN );
	} else {
			imgURN = defaultURN;
			paramURN = defaultURN;
	}
	imgRoi = urnRoi(paramURN);

    var mainViewWidth = 1000;

	var widthFactor, heightFactor, topOffset;
		if (imgRoi != ""){
				widthFactor = parseFloat(imgRoi.split(",")[2]);
				heightFactor = parseFloat(imgRoi.split(",")[3]);
				topOffset = parseFloat(imgRoi.split(",")[1]); leftOffset = parseFloat(imgRoi.split(",")[0]);
                mainViewWidth = Math.floor(1000 / (widthFactor * 1.1) );
		} else {
				widthFactor = 1; heightFactor = 1;
				topOffset = 0; leftOffset = 0;
	            mainViewWidth = 1000 / widthFactor;
		}

	// Populate image fields
	$("img.jcrop-preview").attr("src",imgSvcURL+"request=GetBinaryImage&urn="+paramURN+"&w="+detailWidth);
	$("img#target").attr("src",imgSvcURL+"request=GetBinaryImage&urn="+paramURN+"&w="+mainViewWidth);

    // Create variables (in this scope) to hold the API and image size
    var jcrop_api,
        boundx,
        boundy,

        // Grab some information about the preview pane
        $preview = $('#preview-pane'),
        $pcnt = $('#preview-pane .preview-container'),
        $pimg = $('#preview-pane .preview-container img'),

        xsize = $pcnt.width(),
        ysize = $pcnt.height();

    $('#target').Jcrop({
      onChange: updatePreview,
      onSelect: updatePreview,
      // aspectRatio: xsize / ysize
    },function(){
      // Use the API to get the real image size
      var bounds = this.getBounds();
      boundx = bounds[0];
      boundy = bounds[1];
      // Store the API in the jcrop_api variable
      jcrop_api = this;

      // Move the preview into the jcrop container for css positioning
      $preview.appendTo(jcrop_api.ui.holder);

    });

    function updatePreview(c)
    {
      if (parseInt(c.w) > 0)
      {
        var rx = xsize / c.w;
        var ry = ysize / c.h;

			$pcnt.css({
				height: Math.round( $pcnt.width() * c.h / c.w ) + 'px'
			});

        xsize = $pcnt.width(),
        ysize = $pcnt.height();

        $pimg.css({
          width: Math.round(rx * boundx) + 'px',
          height: Math.round(ry * boundy) + 'px',
          marginLeft: '-' + Math.round(rx * c.x) + 'px',
          marginTop: '-' + Math.round(ry * c.y) + 'px'
        });
		// update info in urnLink
		// 			ysize = size of selection vertically
		// 			xsize = size of selection horizontally
		// 			c.y = top of selection (from top, in pixels)
		// 			c.y = left of select (from top, in pixels)
		//
		//     t = (t * heightFactor) + topOffset;
		//     l = (l * widthFactor) + leftOffset;
		//     w = w * widthFactor;
		//     h = h * heightFactor;
		//

		var tempTop, tempLeft, tempWidth, tempHeight;

		tempLeft = c.x / boundx;
		tempTop = c.y / boundy;
		tempWidth = c.w / boundx;
		tempHeight = c.h / boundy;

		tempLeft = (tempLeft * widthFactor) + leftOffset;
		tempTop = (tempTop * heightFactor) + topOffset;
		tempWidth = tempWidth * widthFactor;
		tempHeight = tempHeight * heightFactor;

		tempLeft = Math.round(tempLeft * 10000 ) / 10000;
		tempTop = Math.round(tempTop * 10000) / 10000;
		tempWidth = Math.round(tempWidth * 10000 ) / 10000;
		tempHeight = Math.round(tempHeight * 10000) / 10000;

		updateData(tempLeft, tempTop, tempWidth, tempHeight);
      }
    };

		//Update data and links
		function updateData(t,l,w,h){
			var newURN, thisPage, gBI, gMV;
			newURN = plainUrn(imgURN) + "@" + t + "," + l + "," + w + "," + h;
			thisPage = ictUrl + "?urn=" + newURN;
			gBI = imgSvcURL + "request=GetBinaryImage&urn=" + newURN + "&w=9000";
			gMV = imgSvcURL + "request=GetIIPMooViewer&urn=" + newURN;
			// display urn
			$("p#urnP").html(newURN);
			// Set urnlink value
			$("a#urnLink").attr("href", newURN);
			// set links
			$("a#zoom").attr("href",thisPage);
			$("a#imageQuotation").attr("href",gBI);
			$("a#contextQuotation").attr("href",gMV);
		}
		//get request parameter
		function get(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
			  return decodeURIComponent(name[1]);
		}

		// return image-urn part of a URN+ROI
		function plainUrn(wholeUrn){
			var tempArray = wholeUrn.split("@");
			var arrayLength = tempArray.length;
			var tempString = tempArray[0];
				return tempString;
		}

		// return roi part of a URN+ROI
		function urnRoi(wholeUrn){
			var tempArray = wholeUrn.split("@");
			var arrayLength = tempArray.length;
			var tempString = "";
			if (arrayLength == 2){
				tempString = tempArray[1];
				return tempString;
			} else {
				return "";
			}

/*
			var tempArray = wholeUrn.split(":");
			var arrayLength = tempArray.length;
			var tempString = "";
			if (arrayLength > 4){
				tempString = tempArray[4];
				return tempString;
			} else {
				return "";
			}
*/
		}

  });
