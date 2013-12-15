$sumoUrl = "http://sourceforge.net/projects/sumo/files/sumo/version%200.19.0/sumo-winbin-0.19.0.zip/download"
$jFuzzyLogicUrl = "http://sourceforge.net/projects/jfuzzylogic/files/jfuzzylogic/jFuzzyLogic.jar/download"
$sumoTarget = "sumo-winbin-0.19.0.zip"
$jFuzzyLogicTarget = "libs\jFuzzyLogic.jar"

$shell=new-object -com shell.application
$CurrentLocation=get-location
$CurrentPath=$CurrentLocation.path

$client = New-Object system.net.WebClient;

Write-Host "Stahovani simulatoru SUMO"
$client.DownloadFile($sumoUrl, $sumoTarget);
Write-Host "Dokonceno"

Write-Host "Stahovani jFuzzyLogic"
$client.DownloadFile($jFuzzyLogicUrl, $jFuzzyLogicTarget);
Write-Host "Dokonceno"

Write-Host "Extrakce SUMO archivu" $sumoTarget
$sumoAbsoluteTarget = $CurrentPath + "\" + $sumoTarget
$sumoZipLocation=$shell.namespace($sumoAbsoluteTarget)
$shell.Namespace($CurrentPath + "\sumo").copyhere($sumoZipLocation.items())
Write-Host "Dokonceno"

Remove-Item $sumoAbsoluteTarget