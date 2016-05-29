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


				// initial values for our object
				// --start--
				ohcoObject.startUrn = range.startContainer.parentNode.getAttribute("data-ctsurn"); 
				ohcoObject.startOffset = range.startOffset;
				ohcoObject.startChar = range.toString()[0];
				ohcoObject.startPos = null;
				ohcoObject.startSs = getSubstring(ohcoObject.startUrn,ohcoObject.startChar,ohcoObject.startOffset);

				// --end--
				ohcoObject.endUrn = range.endContainer.parentNode.getAttribute("data-ctsurn");
				ohcoObject.endOffset = range.endOffset;
				ohcoObject.endChar = range.toString()[range.toString().length - 1];
				ohcoObject.endSs = getSubstring(ohcoObject.endUrn,ohcoObject.endChar,(ohcoObject.endOffset -1));
				ohcoObject.endPos = null;

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
				oO.startSs = "";
				oO.startPos = "empty";
			} else {
				// Drop down to next citation, use first char.
				
				oO.startUrn = getNextLeafUrn( oO.startUrn );
				oO.startSs = getFirstSubstring( oO.startUrn );
				oO.startPos = "beginning";
			}
		} else { // not at end, not empty, so grab the next valid char
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
	nextChar = ls[i];
	//now we need its index in the leaf-node
	// now get the index for that char
	
	var sanitized;
	if (ls[i] == "."){ sanitized = "\\."; } else { sanitized = ls[i]; }
	var newIndex = ls.match(new RegExp(ls[i], "g") || []).length;
	return nextChar + "[" + newIndex + "]";
}

// Gets the next valid non-whitespace character, returns a cts-urn substring
function getPrevValidSubstring(urn,offset){
	var ls = $("[data-ctsurn='" + urn + "']").text();
	var prevChar = "";
	for (var i = offset - 1; i >= 0; i--){
		if ( ls[i].match(/\S/) ){ break; }
	}
	//so i is the next legit char. 
	prevChar = ls[i];
	//now we need its index in the leaf-node
	// now get the index for that char
	var sanitized;
	if (ls[i] == "."){ sanitized = "\\."; } else { sanitized = ls[i]; }
	var newIndex = ls.match(new RegExp(ls[i], "g") || []).length;
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
	var leafString = $("[data-ctsurn='" + urn + "']").text();
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
	var htmlText = "";
	htmlText += "<li><p class='urnListItem'>";
	htmlText += urn;
	htmlText += "</p><p class='urnListNote'></p><i class='fa fa-edit'></i><i class='fa fa-close deleteListItem'></i></li>";
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

