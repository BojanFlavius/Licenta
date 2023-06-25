<?php

require '../vendor/autoload.php';

use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;

class firebaselib
{
    protected $database;

    public function __construct(){
        $factory = (new Factory)->withServiceAccount('findme.json')->withDatabaseUri('https://findme-80cd7-default-rtdb.firebaseio.com');
        $this->database = $factory->createDatabase();
    }
    
    public function insert($id, array $data) {
        if (empty($data) || !isset($data)) { return FALSE; }
        $ref = $this->database->getReference('Users/' . $id)->getChild("location")->update($data);
        return $ref->getKey();
    }
}

$gps = new firebaselib();

var_dump($_GET);
$id = htmlentities($_GET["d"]);
$lat = htmlentities($_GET["la"]);
$lon = htmlentities($_GET["lo"]);
// echo($id);
// echo($lat);
// echo($lon);

var_dump($gps->insert(
    $id,
    [
        'lat' => $lat,
        'lon' => $lon,
    ]
));

?>