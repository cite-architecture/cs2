	// Crappy, lazy way to keep track of how many URNs have been made
	var selectedUrnCounter = 0;

function selectionToUrn() {
	var range, sel;
	var ohcoObject = new Object();
	var finalUrn = ""; 
	var tempNodeStr = "";

	sel = window.getSelection();
	if (sel.getRangeAt) {
		if (sel.rangeCount > 0) {
			range = sel.getRangeAt(0);
		}
	} else {
		// Old WebKit
		range = document.createRange();
		range.setStart(sel.anchorNode, sel.anchorOffset);
		range.setEnd(sel.focusNode, sel.focusOffset);

		//  Handle the case when the selection was selected backwards (from the end to the start in the document)
		if (range.collapsed !== sel.isCollapsed) {
			range.setStart(sel.focusNode, sel.focusOffset);
			range.setEnd(sel.anchorNode, sel.anchorOffset);
		}
	}

	if (range) {
		if( (   range.startContainer == range.endContainer) 
				&& ( range.startOffset == range.endOffset)  ){
			if ( range.startContainer.parentNode.nodeName == "MARK"){
				finalUrn = range.startContainer.parentNode.getAttribute("data-ctsurn");
			} else {
				finalUrn = "selection out of bounds";
			}
		} else if( ( range.startContainer == range.endContainer) 
				&& ( ( range.startOffset + 1 ) == range.endOffset)  ){
			if ( range.startContainer.parentNode.nodeName == "MARK"){
				if (range.toString()[0].match(/\s/)) {
					finalUrn = "This tool does not support citation of white space.";
				} else {
					finalUrn = range.startContainer.parentNode.getAttribute("data-ctsurn") + "@" + 
						getSubstring(
								range.startContainer.parentNode.getAttribute("data-ctsurn"),
								range.toString()[0],
								range.startOffset);
				}
			} else {
				finalUrn = "The selection is out of bounds.";
			}
		} else { // actual range, not just point
			if ( range.startContainer.parentNode.nodeName == "MARK"
					&& range.endContainer.parentNode.nodeName == "MARK"){

				// In case there is mixed content in parent element, we need to do all of this
				var addToStartOffset = 0;
				for (var n = 0; n < range.startContainer.parentNode.childNodes.length; n++){
					if ( range.startContainer.parentNode.childNodes[n] == range.startContainer ){
						break;
					}
					if ( range.startContainer.parentNode.childNodes[n].nodeName == "#text" ){
						addToStartOffset += range.startContainer.parentNode.childNodes[n].length;
					}
				}
				var addToEndOffset = 0;
				for (var n = 0; n < range.endContainer.parentNode.childNodes.length; n++){
					if ( range.endContainer.parentNode.childNodes[n] == range.endContainer ){
						break;
					}
					if ( range.startContainer.parentNode.childNodes[n].nodeName == "#text" ){
						addToEndOffset += range.endContainer.parentNode.childNodes[n].length;
					}
				}

				
				// initial values for our object
				// --start--
				ohcoObject.startUrn = range.startContainer.parentNode.getAttribute("data-ctsurn"); 
				ohcoObject.startOffset = range.startOffset + addToStartOffset;
				ohcoObject.startChar = range.toString()[0];
				ohcoObject.startPos = null;
				ohcoObject.startSs = getSubstring(ohcoObject.startUrn,ohcoObject.startChar,ohcoObject.startOffset);
				console.log("in s2u sOffset: " + ohcoObject.startOffset);
				console.log("in s2u sChar: " + ohcoObject.startChar);

				// --end--
				ohcoObject.endUrn = range.endContainer.parentNode.getAttribute("data-ctsurn");
				ohcoObject.endOffset = range.endOffset + addToEndOffset;
				ohcoObject.endChar = range.toString()[range.toString().length - 1];
				ohcoObject.endSs = getSubstring(ohcoObject.endUrn,ohcoObject.endChar,(ohcoObject.endOffset -1));
				ohcoObject.endPos = null;
				console.log("in s2u eOffset: " + ohcoObject.endOffset);
				console.log("in s2u eChar: " + ohcoObject.endChar);


				finalUrn = cleanUpUrn(ohcoObject);

			} else {
				finalUrn = "selection out of bounds.";
			}
		}	
	} else { // no range
		finalUrn = "unable to capture selection range.";
	}	

	return finalUrn;
}

function cleanUpUrn(ohcoObject){
	var oO = new Object();
	var finalUrn = "";
	oO = ohcoObject;

	//validate startUrn
	if (oO.startChar.match(/\s/)){
		if (  startAtEnd(oO.startUrn, oO.startOffset)) {
			if ( emptyCitation(oO.startUrn) ){
				console.log("I think startUrn is empty.");
				oO.startSs = "";
				oO.startPos = "empty";
			} else {
				// Drop down to next citation, use first char.
			    console.log("I think startUrn is at the end.");
				oO.startUrn = getNextLeafUrn( oO.startUrn );
				oO.startSs = getFirstSubstring( oO.startUrn );
				oO.startPos = "beginning";
			}
		} else { // not at end, not empty, so grab the next valid char
			console.log("Trying to get next valid char.");
			oO.startSs = getNextValidSubstring(oO.startUrn,oO.startOffset);
			if ( oO.startSs == getFirstSubstring(oO.startUrn) ) {
				oO.startPos = "beginning";
			}

		}

	} else if ( oO.startSs == getFirstSubstring(oO.startUrn) ) {
		oO.startPos = "beginning";
	}

	//validate endUrn
	if (ohcoObject.endChar.match(/\s/)){
		if (  endAtStart(oO.endUrn,oO.endOffset) ){
			if ( emptyCitation(oO.endUrn) ){
				oO.endSs = "";
				oO.endPos = "empty";
			} else {
				// jump up to prev citation, use last char
				oO.endUrn = getPrevLeafUrn( oO.endUrn );
				oO.endSs = getLastSubstring (oO.endUrn );
				oO.endPos = "end";
			}
		} else { // not at end, not empty, so grab the next valid char
			oO.endSs = getPrevValidSubstring(oO.endUrn,oO.endOffset);
		}
	} else if ( oO.endSs == getLastSubstring( oO.endUrn ) ) {
		oO.endPos = "end";
	}

	finalUrn = objectToUrn(oO);
	return finalUrn;

}

// Assembles an efficient URN from an ohcoObject
function objectToUrn(oO){
	var urn = "";

	//Construct URN
	var startPassageComponent = oO.startUrn.split(":")[4];
	var endPassageComponent = oO.endUrn.split(":")[4];
	var urnWithoutPassage = oO.startUrn.substring(
			0,
			oO.startUrn.length - startPassageComponent.length
			);

	urn = urnWithoutPassage ;

	// Possibilities
	// sPC == ePC
	// 		sPos=beginning; ePos = end --> single leaf node: sPC
	// sPC == ePC
	// 		sPos=middle,end; ePos=middle,end --> regular
	// sPC != ePC
	// 		sPos=beginning:  first element of range is leaf-node w/o substring
	// 		ePos=end: second element of range is leaf-node w/o substring 

	if (startPassageComponent == endPassageComponent){
		if ((oO.startPos == "beginning" ) && (oO.endPos == "end")){
			urn += startPassageComponent;
		} else {
			urn +=  startPassageComponent
				+ "@" + oO.startSs
				+ "-" + endPassageComponent + "@" 
				+ oO.endSs;
		}
	} else {
		if ( oO.startPos == "beginning"){
			urn +=  startPassageComponent + "-";
		} else if ( oO.startPos == "empty"){
			urn += startPassageComponent + "-";
		} else {
			urn +=  startPassageComponent
				+ "@" + oO.startSs + "-";
		}
		if ( oO.endPos == "end"){
			urn += endPassageComponent;
		} else if ( oO.endPos == "empty"){
			urn += endPassageComponent;
		} else {
			urn += endPassageComponent + "@" 
				+ oO.endSs;
		}
	}

	return urn;

}

// Gets the next valid non-whitespace character, returns a cts-urn substring
function getNextValidSubstring(urn,offset){
	var ls = $("[data-ctsurn='" + urn + "']").text();
	var nextChar = "";
	for (var i = offset; i < ls.length; i++){
		if ( ls[i].match(/\S/) ){ break; }
	}
	//so i is the next legit char. 
	console.log("Now i = " + i + ".");
	nextChar = ls[i];
	//now we need its index in the leaf-node
	// now get the index for that char
	
	var sanitized;
	if (ls[i] == "."){ sanitized = "\\."; } else { sanitized = ls[i]; }
	var newIndex = 0;
	for (var c = 0; c <= i; c++){
		if (ls[c] == ls[i]){ newIndex++; }	
	}

	console.log("New index = " + newIndex + ".");
	return nextChar + "[" + newIndex + "]";
}

// Gets the next valid non-whitespace character, returns a cts-urn substring
function getPrevValidSubstring(urn,offset){
	var ls = $("[data-ctsurn='" + urn + "']").text();
	var prevChar = "";
	console.log("Text-content of node: " + ls);
	console.log("in gnvss, offset = " + offset);
	console.log("Character " + (offset+1) + " of ls is '" + ls[offset+1] + "'.");
	for (var i = offset - 1; i >= 0; i--){
		if ( ls[i].match(/\S/) ){ break; }
	}
	//so i is the next legit char. 
	console.log("Now i = " + i + ".");
	prevChar = ls[i];
	//now we need its index in the leaf-node
	// now get the index for that char
	var sanitized;
	if (ls[i] == "."){ sanitized = "\\."; } else { sanitized = ls[i]; }
	var newIndex = 0;
	for (var c = 0; c <= i; c++){
		if (ls[c] == ls[i]){ newIndex++; }	
	}

	console.log("New index = " + newIndex + ".");
	return prevChar + "[" + newIndex + "]";
}


// True if there are no valid (non-white-space) characters between offset and the end of leaf-node@urn
function startAtEnd( urn, offset ){
	var ls = $("[data-ctsurn='" + urn + "']").text();
	var atEnd = true;
	if (offset > 0){ offset-- }	
	// check that offset is white-space
	if ( ls[offset].match(/\s/) ){
		for (var i = offset; i < ls.length; i++){
			if ( ls[i].match(/\S/) ){
				atEnd = false;
				break;
			}
		}
	} else {
		atEnd = false;
	}
	return atEnd;
}


// true if there are no non-whitespace characters in the <mark> element
function emptyCitation( urn ){
	return $("[data-ctsurn='" + urn + "']").text().match(/^\s+$/);
}

// True if there are no valid (non-white-space) characters between the beginning of leaf-node@urn and offset
function endAtStart( urn, offset ){
	var ls = $("[data-ctsurn='" + urn + "']").text();
	var atStart = true;
	// check that offset is white-space
	if ( ls[offset].match(/\s/) ){
		for (var i = 0; i < offset; i++) {
			if ( ls[i].match(/\S/) ){
				atStart = false;
				break;
			}
		}
	} else {
		atStart = false;
	}
	return atStart;
}

function getSubstring(urn,testChar,offset){
	//console.log ("offset as param: " + offset + "; char as param: " + testChar);
	var leafString = $("[data-ctsurn='" + urn + "']").text();
	//console.log( "leaf string: " + leafString );
	var urnIndex = 1;
	for (var i = 0; i < offset; i++){
		if (leafString[i] == testChar){
			urnIndex++;
		}
	}
	return testChar + "[" + urnIndex + "]";
}

function getFirstSubstring(urn){
	var leafString = $("[data-ctsurn='" + urn + "']").text();
	for (var i = 0; i < leafString.length; i++){
		if ( leafString[i].match(/\S/) ){ break; }
	}
	return leafString[i] + "[1]";
}

function getLastSubstring(urn){
	var leafString = $("[data-ctsurn='" + urn + "']").text();
	// find last valid char
	for (var i = (leafString.length - 1) ; i > 0; i--){
		if ( leafString[i].match(/\S/) ){ break; }
	}
	// now get the index for that char
	var sanitized;
	if (leafString[i] == "."){ sanitized = "\\."; } else { sanitized = leafString[i]; }
	var newIndex = leafString.match(new RegExp(sanitized, "g") || []).length;

	return leafString[i] + "[" + newIndex + "]";

}

function getNextLeafUrn(urn){
	var nextUrn = $("[data-ctsurn='" + urn + "']").next().attr("data-ctsurn");
	return nextUrn;
}
function getPrevLeafUrn(urn){
	var prevUrn = $("[data-ctsurn='" + urn + "']").prev().attr("data-ctsurn");
	return prevUrn;
}


function displaySelectedUrn(urnMsg, valid){
	var messageClass = "";
	var messageStr = "";

	if (valid){
		messageClass = "valid";
		$("#selectedUrn .fa").removeClass("hide");
	} else {
		messageClass = "invalid";
		$("#selectedUrn .fa").addClass("hide");
	}

	messageStr = urnMsg;
	$("#selectedUrn p").html(messageStr);
	$("#selectedUrn p").attr("class",messageClass);
}

jQuery(document).ready(function($) {


	// in the off-chance there is an empty, citable element, this will give the user something to hit
	var nbss = "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;";
	$('mark')
		.filter(function() {
			return this.innerHTML.match(/^\s+$/);    
		}).append(nbss) ;

	// do the work
	$('#cts-textReply').on('mouseup', function(e)
			{
				var selectedUrn = "";
				try {
					selectedUrn = selectionToUrn();
					displaySelectedUrn(selectedUrn,( selectedUrn.startsWith("urn:cts:") ))
				} catch(e) {
					displaySelectedUrn("invalid",false);
					console.log(e)
				}
			});

	// Activate buttons
	$('.fa-clipboard').on('mouseup', function(e)
			{
				var exportText = gatherAllUrns();
				$("div#clipBoard textarea").val(exportText);
				$("div#clipBoard").show();
				$("div#clipBoard textarea").select();

			});

	//Stuff for the note-adding dialogue box
	$("#addNoteButton").on('mousedown', function(e){
		$(this).addClass("hilite");
	});
	$("#addNoteButton").on('mouseup', function(e){
		$(this).removeClass("hilite");
		var noteText = $("div#addNote input:text").val();
		var re = /\s+/gi;
		noteText = noteText.replace(re," ");
		var noteIndex = $("div#addNote").attr("itemindex");
		$("ul#urnList li").eq(noteIndex).children("p.urnListNote").html(noteText);
		$("div#addNote input:text").val('');
		$("div#addNote").addClass("hide");
	});
	$("#cancelNoteButton").on('mousedown', function(e){
		$(this).addClass("hilite");
	});
	$("#cancelNoteButton").on('mouseup', function(e){
		$(this).removeClass("hilite");
		$("div#addNote input:text").val();
		$("div#addNote").addClass("hide");
	});


	// remove or edit list-items

	$(".fa-plus-square").on("mouseup", function(e)
			{
				var exportText = "";
				$("ul#urnList").append(generateUrnListItem( $(this).next("p").text() ) );

				$('.fa-close, .deleteListItem').on('mouseup', function(e){
					var whichUrn = $(this).parents("li").children("p.urnListItem").attr("data-ctsselectedurn");
					var parentNodes = [];

					$("mark[data-ctsselectedurn='" + whichUrn + "']").parents("mark").each( function() {
						parentNodes.push(this);	
					});
					//console.log(parentNodes.length);
					$("mark[data-ctsselectedurn='" + whichUrn + "']").remove();

					for (var i = 0; i < parentNodes.length; i++){
						parentNodes[i].normalize();
					}

					$(this).parents("li").remove();
				});
				$('.fa-edit').on('mouseup', function(e){
					$("div#addNote").removeClass("hide");

					// Pre-load the box with any existing note
					if( $(this).prev("p.urnListNote").text() != ""){
						$("div#addNote input:text").val( $(this).prev("p.urnListNote").text() );
					}

					// Associate note with URN
					$("div#addNote p#noteUrn").html( $(this).parents("li").children("p.urnListItem").text() );
					$("div#addNote").attr("itemIndex", $(this).parents("li").index() );


				});


			});

	 $("i#closeClipboard").on("mouseup", function(e) {
		 $("div#clipBoard").hide();
		 //$("div#clipBoard textarea").val('');
	 });

});

function generateUrnListItem(urn){
	selectedUrnCounter++;
	var htmlText = "";
	var markerText = "<mark class='bracket bracketMarker' style='background-color: " + colorForIndex(selectedUrnCounter) + "' ";
	markerText += " data-ctsselectedindex='" + selectedUrnCounter + "' />";
	htmlText += "<li><p class='urnListItem' data-ctsSelectedUrn='" + urn + "' data-urnCounter='" + selectedUrnCounter + "' >";
	htmlText += markerText + urn;
	htmlText += "</p><p class='urnListNote'></p><i class='fa fa-edit'></i><i class='fa fa-close deleteListItem'></i></li>";
	// TESTING!
	markSelectedUrn(urn, selectedUrnCounter);
	return htmlText;
}

function gatherAllUrns(){
	var tsv = "urn\tnote\r";
	$("ul#urnList li").each( function(){
		tsv += $(this).children(".urnListItem").text();
		tsv += "\t";
		tsv += $(this).children(".urnListNote").text();
		tsv += "\r";
	});
	return tsv;

}

function markSelectedUrn(urn,counter){
	var urnPassage = urn.split(":")[4];
	var openUrn = "";
	var openLeafUrn = "";
	var closeUrn = "";
	var closeLeafUrn = "";
	var openChar = "";
	var openCharIndex = 0;
	var closeChar = "";
	var closeCharIndex = 0;
	var tempRef = "";
	var tempPassage = "";
	var tempSs = "";
	var urnWithoutPassage = urn.substring(
			0,
			urn.length - urnPassage.length
			);
	// single leaf-node or range?
	if (urnPassage.indexOf("-") != -1){
		//deal with first component
		tempRef = urnPassage.split("-")[0];
		openUrn = urnWithoutPassage + tempRef;
		if ( tempRef.indexOf("@") != -1){
			tempPassage = tempRef.split("@")[0];
			tempSs = tempRef.split("@")[1];
			openChar = charFromSubref(tempSs);
			openCharIndex = indexFromSubref(tempSs);
			openLeafUrn = urnWithoutPassage + tempPassage;
			
			markBeforeChar(urn,openLeafUrn,openUrn,openChar,openCharIndex,counter);				
		} else {
			markBeforeLeaf(urn,openUrn,counter);
		}
			
		//deal with second component
		tempRef = urnPassage.split("-")[1];
		closeUrn = urnWithoutPassage + tempRef;
		if ( tempRef.indexOf("@") != -1){
			tempPassage = tempRef.split("@")[0];
			tempSs = tempRef.split("@")[1];
			closeChar = charFromSubref(tempSs);
			closeCharIndex = indexFromSubref(tempSs);
			closeLeafUrn = urnWithoutPassage + tempPassage;
			markAfterChar(urn,closeLeafUrn,closeUrn,closeChar,closeCharIndex,counter);				
		} else {
			markAfterLeaf(urn,closeUrn,counter);
		}

	} else {
		if ( urnPassage.indexOf("@") == -1 ){
			openUrn = urn;
			closeUrn = urn;
			markAfterLeaf(urn,closeUrn,counter);
			markBeforeLeaf(urn,openUrn,counter);
		} else {
			tempPassage = tempRef.split("@")[0];
			tempSs = tempRef.split("@")[1];
			openChar = charFromSubref(tempSs);
			openCharIndex = indexFromSubref(tempSs);
			closeChar = charFromSubref(tempSs);
			closeCharIndex = indexFromSubref(tempSs);
			markBeforeChar(urn,openChar,openCharIndex,counter);				
			markAfterChar(urn,closeChar,closeCharIndex,counter);				
		}
	}
}

function markBeforeLeaf(urn,openUrn,index){
	var markText = "<mark class='bracket left-bracket' ";
	markText += "data-ctsselectedurn='" + urn + "' ";
	markText += "data-ctsselectedindex='" + index + "' ";
	markText += " style='background-color: " + colorForIndex(index) + "' />";
	$("mark[data-ctsurn='" + openUrn + "']").prepend(markText);
}
function markAfterLeaf(urn,closeUrn,index){
	var markText = "<mark class='bracket right-bracket' ";
	markText += "data-ctsselectedurn='" + urn + "' ";
	markText += "data-ctsselectedindex='" + index + "' ";
	markText += " style='background-color: " + colorForIndex(index) + "' />";
	$("mark[data-ctsurn='" + closeUrn + "']").append(markText);
}

function colorForIndex(index){
	var colors = ["CornflowerBlue","BurlyWood","LightCyan","DarkKhaki","DarkSalmon","DarkSeaGreen","Silver","GoldenRod","Khaki","LightBlue","LightSkyBlue","MediumOrchid","MistyRose","NavajoWhite","Olive","Orchid","Peru","RosyBrown","RebeccaPurple","Silver","Tan","YellowGreen"];
	return colors[index % colors.length];
}

function charFromSubref(sr){
	return sr.split("[")[0];
}

function indexFromSubref(sr){
	var tmpString = sr.split("[")[1];
	return tmpString.split("]")[0];
}

function markBeforeChar(urn,openLeafUrn,passageUrn,myChar,myIndex,counter){
	var tempHtml = $("mark[data-ctsurn='" + openLeafUrn + "']").html();

	var markText = "<mark class='bracket left-bracket' ";
	markText += "data-ctsselectedurn='" + urn + "' ";
	markText += "data-ctsselectedindex='" + counter + "' ";
	markText += " style='background-color: " + colorForIndex(counter) + "' />";

	var htmlArray = tempHtml.split('');
	var outHtml = "";
	var inTag = false;
	var tempChar = "";
	var incrementer = 0;
	for (i = 0; i < htmlArray.length; i++){
		tempChar = htmlArray[i];		
		if ( tempChar == "<"){ inTag = true; }	
		if ( inTag != true ){
			if ( tempChar == myChar){
				incrementer++;
				if (incrementer == myIndex){
					outHtml += markText;	
				}
			}
		}
		outHtml += tempChar;
		if ( tempChar == ">") { inTag = false; }
	}
	$("mark[data-ctsurn='" + openLeafUrn + "']").html(outHtml);
}

function markAfterChar(urn,closeLeafUrn,passageUrn,myChar,myIndex,counter){
	var tempHtml = $("mark[data-ctsurn='" + closeLeafUrn + "']").html();

	var markText = "<mark class='bracket right-bracket' ";
	markText += "data-ctsselectedurn='" + urn + "' ";
	markText += "data-ctsselectedindex='" + counter + "' ";
	markText += " style='background-color: " + colorForIndex(counter) + "' />";

	var htmlArray = tempHtml.split('');
	var outHtml = "";
	var inTag = false;
	var tempChar = "";
	var incrementer = 0;
	for (i = 0; i < htmlArray.length; i++){
		tempChar = htmlArray[i];		
		outHtml += tempChar;
		if ( tempChar == "<"){ inTag = true; }	
		if ( inTag != true ){
			if ( tempChar == myChar){
				incrementer++;
				if (incrementer == myIndex){
					outHtml += markText;	
				}
			}
		}
		if ( tempChar == ">") { inTag = false; }
	}
	$("mark[data-ctsurn='" + closeLeafUrn + "']").html(outHtml);
}

