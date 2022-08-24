<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    const data = JSON.parse('${requestScope.get("kpacs")}');
</script>

<script src="./js/lib/grid/codebase/grid.min.js"></script>
<link rel="stylesheet" href="./js/lib/grid/codebase/grid.css">

<script src="./js/kpacs.js"></script>

<div id="container"></div>

<div>
    <h3>Add new K-PAC</h3>
    <form  id="add-form">
        <p><label>Title: <input type="text" name="title" maxlength="250"></label></p>
        <p><label>Description: <input type="text" name="description" maxlength="2000"></label></p>
        <p><button type="submit">Add</button></p>
    </form>
</div>
</body>
</html>