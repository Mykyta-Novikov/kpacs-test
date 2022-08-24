<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    const data = JSON.parse('${requestScope.get("setContents")}');
</script>

<script src="./../../js/lib/grid/codebase/grid.min.js"></script>
<link rel="stylesheet" href="./../../js/lib/grid/codebase/grid.css">

<script src="./../../js/setContents.js"></script>

<div style="height:500px; width:600px" id="container"></div>
</body>
</html>