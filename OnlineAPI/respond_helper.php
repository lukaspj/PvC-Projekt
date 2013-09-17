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
if(!function_exists('resp_app_user_exists'))
{
	function resp_app_user_exists($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		return $obj->app_model->usernameavailable($user) ? "1" : "0";
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

/** END OF FILE **/
