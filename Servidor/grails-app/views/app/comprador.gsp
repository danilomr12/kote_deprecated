<%@ page contentType="text/html;charset=UTF-8" %>
<!-- saved from url=(0014)about:internet -->
<html lang="en">

<!--
Smart developers always View Source.

This application was built using Adobe Flex, an open source framework
for building rich Internet applications that get delivered via the
Flash Player or to desktops via Adobe AIR.

Learn more about Flex at http://flex.org
// -->

<head>
<title>Comprador</title>
<script src="${resource(dir:'flex', file:'AC_OETags.js')}" language="javascript" type="text/javascript"></script>
<style type="text/css">
body { margin: 0px; overflow:hidden }
</style>
<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 10;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 0;
// -----------------------------------------------------------------------------
// -->
</script>
</head>

<body scroll="no">
<script language="JavaScript" type="text/javascript">
<!--
// Version check based upon the values entered above in "Globals"
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);


// Check to see if the version meets the requirements for playback
if (hasRequestedVersion) {  // if we've detected an acceptable version
    	// if we've detected an acceptable version
		// embed the Flash Content SWF when all tests are passed
		AC_FL_RunContent(
					"src", "/flex/Comprador",
					"width", "100%",
					"height", "100%",
					"align", "middle",
					"id", "Comprador",
					"quality", "high",
					"bgcolor", "#C0C0C0",
					"name", "Comprador",
					"allowScriptAccess","sameDomain",
					"type", "application/x-shockwave-flash",
					"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
// -->
</script>
<noscript>
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="Comprador" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="${resource(dir:'flex',file:'Comprador.swf')}"/>
			<param name="quality" value="high" />
			<param name="bgcolor" value="#C0C0C0" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="${resource(dir:'/flex',file:'Comprador.swf')}" quality="high" bgcolor="#C0C0C0"
				width="100%" height="100%" name="Comprador" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</noscript>
</body>
</html>