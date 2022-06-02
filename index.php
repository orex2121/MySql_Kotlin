<?php 
 
 //Добавление операционного файла БД
 require_once '../mysql_artist/DbOperation.php';
 
 //Массив ответов
 $response = array(); 
 
 //Вызываем API если запрос GET с именем "op"
 if(isset($_GET['op'])){
 
     //Переключатиль значения операции
     switch($_GET['op']){
     
         //Если в запросе получен "addartist"
         //мы добавим исполнителя
         case 'addartist':
         if(isset($_POST['name']) && $_POST['name'] != "" && isset($_POST['genre'])){
             $db = new DbOperation(); 
             if($db->createArtist($_POST['name'], $_POST['genre'])){
                 $response['error'] = false;
                 $response['message'] = 'Артист добавлен удачно.';
             }else{
                 $response['error'] = true;
                 $response['message'] = 'Не получилось добавить артиста!!!';
             }
         }else{
             $response['error'] = true; 
             $response['message'] = 'Необходимые параметры отсутствуют!!!';
         }
         break; 
         
         
         // Если в запросе получен "delartist" то мы удаляем
         case 'delartist':
         if(isset($_POST['id']) && $_POST['id'] != ""){
             $db = new DbOperation();
             if($db->delArtist($_POST['id'])){
                 $response['error'] = false;
                 $response['message'] = 'Артист удален.';
             }else{
                 $response['error'] = true;
                 $response['message'] = 'Не получилось удалить артиста!!!';
             }
         }else{
             $response['error'] = true; 
             $response['message'] = 'Необходимые параметры отсутствуют!!!';
         }
         break;
         
         // Если в запросе получен "updartist" то мы удаляем
         case 'updartist':
         if(isset($_POST['id']) && $_POST['id'] != "" && isset($_POST['name']) && $_POST['name'] != "" && isset($_POST['genre'])){
             $db = new DbOperation();
             if($db->updArtist($_POST['id'], $_POST['name'], $_POST['genre'])){
                 $response['error'] = false;
                 $response['message'] = 'Артист обновлен.';
             }else{
                 $response['error'] = true;
                 $response['message'] = 'Не получилось обновить артиста!!!';
             }
         }else{
             $response['error'] = true; 
             $response['message'] = 'Необходимые параметры отсутствуют!!!';
         }
         break;
         
         //Если в запросе получен "getartist" то мы извлекаем все данные
         case 'getartists':
             $db = new DbOperation();
             $artists = $db->getArtists();
             if(count($artists)<=0){
                 $response['error'] = true; 
                 $response['message'] = 'В базе данных ничего не найдено';
             }else{
                 $response['error'] = false; 
                 $response['artists'] = $artists;
             }
             break; 
             
         default:
         $response['error'] = true;
         $response['message'] = 'Нет операции для выполнения';
         
     }
 
 }else{
     $response['error'] = false; 
     $response['message'] = 'Неверный запрос';
 }
 
 //Отобразить данные в JSON
 echo json_encode($response);
 
