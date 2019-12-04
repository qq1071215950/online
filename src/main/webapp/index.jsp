<html>
<body>
<h2>Hello World!</h2>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="文件上传">
</form>

<form name="form2" action="/manage/product/richtextImgUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="文本上传">
</form>
</body>
</html>
