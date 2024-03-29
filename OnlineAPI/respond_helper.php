<?php

/**********************************/
/************ LOGINS **************/
/**********************************/

if ( ! function_exists('resp_login_check'))
{
	function resp_login_check($obj, $data)
	{
		$obj->load->helper('user');
		if(is_logged_in($obj))
		{
			if(UserFlag_isset($obj->session->userdata('flags'), 1))
				echo "username|".$obj->session->userdata('usn')."|usermenu|$data[adminmenu]|status|Logged in as:|green";
			else {
				echo "username|".$obj->session->userdata('usn')."|usermenu|$data[usermenu]|status|Logged in as:|green";
			}
		}
		else
		{
			echo "username|$data[loginmenu]|usermenu||status|Not logged in!|red";
		}
	}
}

if ( ! function_exists('resp_login'))
{
	function resp_login($obj, $data)
	{
		$success = user_login($obj);
		if($success)
			if(UserFlag_isset($obj->session->userdata('flags'), 1))
				echo "username|".$obj->session->userdata('usn')."|usermenu|$data[adminmenu]|status|Logged in as:|green";
			else {
				echo "username|".$obj->session->userdata('usn')."|usermenu|$data[usermenu]|status|Logged in as:|green";
			}
		else
			echo "username|$data[loginmenu]|usermenu||status|Error logging in!|red";
	}
}

if ( ! function_exists('resp_logout'))
{
	function resp_logout($obj, $data)
	{
		$success = user_logout($obj);
		if($success)
		echo "username|$data[loginmenu]|usermenu||status|Logged out!|red";
	}
}

if ( ! function_exists('resp_register'))
{
	function resp_register($obj, $data)
	{
		$usn = $obj->input->post('usn');
		$pass = $obj->input->post('pass');
		$email = $obj->input->post('email');
		return add_user($usn, $pass, $email);
	}
}

/**********************************/
/********** INFOBLOCKS ************/
/**********************************/

if ( ! function_exists('resp_add_infoblock'))
{
	function resp_add_infoblock($obj, $data)
	{
		$obj->load->model('frontpage_model');
		return $obj->frontpage_model->addInfoblock($obj, $obj->input->post('title'), $obj->input->post('logo'), $obj->input->post('description'), $obj->input->post('link'));
	}
}

if ( ! function_exists('resp_get_infoblock_list'))
{
	function resp_get_infoblock_list($obj)
	{
		return get_infoblock_list($obj);
	}
}

if ( ! function_exists('resp_get_infoblock_imgs'))
{
    function resp_get_infoblock_imgs(&$data)
    {
		$abs_path = str_replace('system/','content/uploads/icons/', BASEPATH);
        //Add a trailing slash if it doesn't exist.
        $abs_path = preg_replace("#([^/])/*$#", "\\1/", $abs_path);
		$site = $abs_path;
		$files = array_diff(scandir($site), array('..', '.'));
		$logolist = '';
		foreach($files as $file)
		{
			if(strpos($file,".") != false)
			{
				$filesplit = preg_split("/\./",$file);
				$logolist .= '<option value="'.$filesplit[0].'">'.$filesplit[0].'</option>';
			}
		}
		return $logolist;
	}
}

if ( ! function_exists('resp_delete_infoblock'))
{
	function resp_delete_infoblock($obj, $data)
	{
		$obj->load->model('frontpage_model');
		$arr = array();
		$arr = json_decode($obj->input->post('infoblocks'));
		echo "WTF";
		foreach($arr as $val)
		{
			$obj->frontpage_model->deleteInfoblock($obj, $val);
		}
	}
}

/**********************************/
/********** SPOTLIGHTS ************/
/**********************************/

if ( ! function_exists('resp_delete_spotlight'))
{
	function resp_delete_spotlights($obj, $data)
	{
		$obj->load->model('frontpage_model');
		$arr = json_decode($obj->input->post('spotlights'));
		foreach($arr as $val)
		{
			$obj->frontpage_model->deleteSpotlight($obj, $val);
		}
	}
}

if ( ! function_exists('resp_add_spotlight'))
{
	function resp_add_spotlight($obj, $data)
	{
		$obj->load->model('frontpage_model');
		$obj->frontpage_model->addSpotlight($obj, $obj->input->post('title'), $obj->input->post('logo'), $obj->input->post('description'), $obj->input->post('link'));
	}
}

if ( ! function_exists('resp_get_spotlight_list'))
{
	function resp_get_spotlight_list($obj)
	{
		return get_spotlight_list($obj);
	}
}

if ( ! function_exists('resp_get_spotlight_imgs'))
{
	function resp_get_spotlight_imgs($obj, $data)
	{
		$abs_path = str_replace('system/','content/uploads/spotlights/', BASEPATH);
        //Add a trailing slash if it doesn't exist.
        $abs_path = preg_replace("#([^/])/*$#", "\\1/", $abs_path);
		$site = $abs_path;
		$files = array_diff(scandir($site), array('..', '.'));
		$spotlist = '';
		foreach($files as $file)
		{
			if(strpos($file,".") != false)
			{
				$filesplit = preg_split("/\./",$file);
				$spotlist .= '<option value="'.$filesplit[0].'">'.$filesplit[0].'</option>';
			}
		}
		return $spotlist;
	}
}

/**********************************/
/********* MISCELLANEOUS **********/
/**********************************/
if ( ! function_exists('resp_delete_file'))
{
	function resp_delete_file($obj, $data)
	{
		$filename = "./content/uploads/".$obj->input->post('type')."/".$obj->input->post('url');
		switch($obj->input->post('type'))
		{
			case 'spotlights':
				$filename = $filename.".jpg";
			break;
			case 'icons':
				$filename = $filename.".png";
			break;
		}
		return unlink($filename)."|".$obj->input->post('url');
	}
}

/**********************************/
/********** ANDROID APP ***********/
/**********************************/
if(!function_exists('resp_app_user_available'))
{
	function resp_app_user_available($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		return $obj->app_model->usernameavailable($user) ? "1" : "0";
	}
}

if(!function_exists('resp_app_verify_user'))
{
	function resp_app_verify_user($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		$pass = $obj->input->post('pass');
		return $obj->app_model->verify_user($user, $pass) ? "1" : "0";
	}
}

if(!function_exists('resp_app_user_register'))
{
	function resp_app_user_register($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		$lat = $obj->input->post('lat');
		$long = $obj->input->post('long');
		$id = $obj->input->post('id');
		$pass = $obj->input->post('password');
		return $obj->app_model->registerUser($user, $lat, $long, $id, $pass);
	}
}

if(!function_exists('resp_all_app_users'))
{
	function resp_all_app_users($obj, $data)
	{
		$obj->load->model('app_model');
		$table = $obj->app_model->allAppUsers();
		foreach($table->result() as $row)
		{
			echo $row->username . "|";
			echo $row->x . "|";
			echo $row->y . "|";
			echo $row->bluetoothid;
			echo "\n";
		}
	}
}

if(!function_exists('resp_app_user_radius_search'))
{
	function resp_app_user_radius_search($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		$lat = $obj->input->post('lat');
		$long = $obj->input->post('long');
		$radius = $obj->input->post('radius');
		$table = $obj->app_model->getUsersInsideCircle($user, $lat, $long, $radius);
		foreach($table->result() as $row)
		{
			echo $row->username . "|";
			echo $row->x . "|";
			echo $row->y . "|";
			echo $row->bluetoothid;
			echo "\n";
		}
	}
}

if(!function_exists('resp_app_update_position'))
{
	function resp_app_update_position($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		$lat = $obj->input->post('lat');
		$long = $obj->input->post('long');
		return $obj->app_model->updatePosition($user, $lat, $long);
	}
}

/**********************************/
/************* JONAS **************/
/**********************************/
if(!function_exists('jon_app_user_exists'))
{
	function jon_app_user_exists($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		return $obj->app_model->jon_userexists($deviceid) ? "1" : "0";
	}
}

if(!function_exists('jon_app_update_position'))
{
	function jon_app_update_position($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$lat = $obj->input->post('lat');
		$long = $obj->input->post('long');
		return $obj->app_model->jon_updatePosition($deviceid, $lat, $long);
	}
}

if(!function_exists('jon_app_initiate_user'))
{
	function jon_app_initiate_user($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$name = $obj->input->post('name');
		return $obj->app_model->jon_initiateUser($deviceid, $name);
	}
}

if(!function_exists('jon_app_get_other_users'))
{
	function jon_app_get_other_users($obj, $data)
	{
		$obj->load->model('app_model');
		$mydeviceid = $obj->input->post('mydeviceid');
		$result = $obj->app_model->jon_getOtherUsers($mydeviceid);
		
		$alluserarray = array();
		$count = 0;
		
		foreach($result->result() as $row){
			$singleuserarray[0] = $row->name;
			$singleuserarray[1] = $row->x;
			$singleuserarray[2] = $row->y;
			$singleuserarray[3] = $row->time;
			
			$alluserarray[$count] = $singleuserarray;	
			$count = $count+1;
		}
		
		return json_encode($alluserarray);
	}
}

if(!function_exists('jon_app_delete_notification'))
{
	function jon_app_delete_notification($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$realid = $obj->input->post('realid');
		return $obj->app_model->jon_deleteNotification($deviceid, $realid);
	}
}

if(!function_exists('jon_app_delete_association'))
{
	function jon_app_delete_association($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$realid = $obj->input->post('realid');
		return $obj->app_model->jon_deleteAssociation($deviceid, $realid);
	}
}

if(!function_exists('jon_app_get_notifications'))
{
	function jon_app_get_notifications($obj, $data)
	{
		$obj->load->model('app_model');
		$mydeviceid = $obj->input->post('mydeviceid');
		$result = $obj->app_model->jon_getNotifications($mydeviceid);
		
		$allnotificationarray = array();
		$count = 0;
		
		foreach($result->result() as $row){
			$singlenotificationarray[0] = $row->realid;
			$singlenotificationarray[1] = $row->message;
			
			$allnotificationarray[$count] = $singlenotificationarray;	
			$count = $count+1;
		}
		
		return json_encode($allnotificationarray);
	}
}

if(!function_exists('jon_app_create_notification'))
{
	function jon_app_create_notification($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$realid = $obj->input->post('realid');
		$message = $obj->input->post('message');
		return $obj->app_model->jon_createNotification($deviceid, $realid, $message);
	}
}

if(!function_exists('jon_app_get_associations'))
{
	function jon_app_get_associations($obj, $data)
	{
		$obj->load->model('app_model');
		$mydeviceid = $obj->input->post('mydeviceid');
		$type = $obj->input->post('type');
		$result = $obj->app_model->jon_getAssociations($mydeviceid, $type);
		
		$allassociationarray = array();
		$count = 0;
		
		foreach($result->result() as $row){
			$singleassociationarray[0] = $row->realid;
			$singleassociationarray[1] = $row->nameid;
			
			$allassociationarray[$count] = $singleassociationarray;	
			$count = $count+1;
		}
		
		return json_encode($allassociationarray);
	}
}

if(!function_exists('jon_app_create_association'))
{
	function jon_app_create_association($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$realid = $obj->input->post('realid');
		$nameid = $obj->input->post('nameid');
		$type = $obj->input->post('type');
		return $obj->app_model->jon_createAssociation($deviceid, $realid, $nameid, $type);
	}
}

if(!function_exists('jon_app_association_exists'))
{
	function jon_app_association_exists($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$real = $obj->input->post('realid');
		return $obj->app_model->jon_associationexists($deviceid, $realid) ? "1" : "0";
	}
}

if(!function_exists('jon_app_notification_exists'))
{
	function jon_app_notification_exists($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		$real = $obj->input->post('realid');
		return $obj->app_model->jon_notificationexists($deviceid, $realid) ? "1" : "0";
	}
}

if(!function_exists('jon_app_delete_user'))
{
	function jon_app_delete_user($obj, $data)
	{
		$obj->load->model('app_model');
		$deviceid = $obj->input->post('deviceid');
		return $obj->app_model->jon_deleteUser($deviceid);
	}
}

/** END OF FILE **/
