<?
// 업로드한 파일이 저장될 디렉토리 정의
$target_dir = "up";  // 서버에 up 이라는 디렉토리가 있어야 한다.

if(strcmp($upfile,"none")) {   // 파일이 업로드되었을 경우

// 업로드 금지 파일 식별 부분
    $filename = explode(".", $upfile_name);
    $extension = $filename[sizeof($filename)-1];
        
    if(!strcmp($extension,"html") ||
       !strcmp($extension,"htm") ||
       !strcmp($extension,"php") ||      
       !strcmp($extension,"inc"))
    {
       echo("File is prohibitted to upload.");
       exit;
    }

// 동일한 파일이 있는지 확인하는 부분
    $target = $target_dir . "/" . $upfile_name;
    if(file_exists($target)) {
       echo("File with same name already exists ");
       exit;
    }

// 지정된 디렉토리에 파일 저장하는 부분
    if(!copy($upfile,$target)) {   // false일 경우
       echo("Saving Failure");
       exit;
    }

// 임시 파일을 삭제하는 부분
    if(!unlink($upfile)) { // false일 경우
       echo("Deleting tmp failed");
       exit;
    }
?>  

<html>
<body>
    <table width="500" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr>
       <td colspan="2" align="center" bgColor="#CCCCCC" width="100%"><font size=2><b>>>
           Success upload <<</b></font></td>  
    </tr>
    <tr>
       <td align="left" bgColor="#EEEEEE" width="120"><font size=2>filename</font></td>
       <td bgColor="#EEEEEE"><font size=2><?echo("$upfile_name")?></font></td>
    </tr>
    <tr>
       <td align="left" bgColor="#EEEEEE" width="120"><font size=2>name for tmp saving</font></td>
       <td bgColor="#EEEEEE"><font size=2><?echo("$upfile")?></font></td>
    </tr>
    <tr>
       <td align="left" bgColor="#EEEEEE" width="120"><font size=2>file size(Bytes)</font></td>
       <td bgColor="#EEEEEE"><font size=2><?echo("$upfile_size")?> Bytes</font></td>
    </tr>
    <tr>
       <td align="left" bgColor="#EEEEEE" width="120"><font size=2>file, MIME Type</font></td>
       <td bgColor="#EEEEEE"><font size=2><?echo("$upfile_type")?></font></td>
    </tr>
    </table>

<?
} else {
?>
        <table width="500" border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="#FFCCFF">
    <tr>
       <td width="100%" align="center" bgColor="#CCCCCC"><font size=2><b>upload failure!!!</b></font></td>
    </tr>
    </table>
</body>
</html>

<?
}
?>
