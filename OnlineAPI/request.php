<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class request extends CI_Controller {

	public function index()
	{
		echo "You may not directly access this page";
	}

	public function get($action)
	{
		//$http_origin = $_SERVER['HTTP_ORIGIN'];
		header("Access-Control-Allow-Origin: http://nakednerd.clan-net.dk");
		$this->load->helper('core');
		$data = initBigCore($this);
		$this->load->helper('user');
		$this->load->helper('respond');
		//$this->load->helper('frontpage');
		switch($action)
		{
			case "login_check":
				echo resp_login_check($this, $data);
			break;
			case "login_user":
				echo resp_login($this, $data);
			break;
			case "logout_user":
				echo resp_logout($this, $data);
			break;
			case "register_user":
				echo resp_register($this, $data);
			break;
			case "delete_spotlights":
				echo resp_delete_spotlights($this, $data);
			break;
			case "add_spotlight":
				echo resp_add_spotlight($this, $data);
			break;
			case "get_spotlight_imgs":
				echo resp_get_spotlight_imgs($this, $data);
			break;
			case "get_spotlight_list":
				echo resp_get_spotlight_list($this);
			break;
			case "get_infoblock_imgs":
				echo resp_get_infoblock_imgs($this, $data);
			break;
			case "get_infoblock_list":
				echo resp_get_infoblock_list($this, $data);
			break;
			case "add_infoblock":
				echo resp_add_infoblock($this, $data);
			break;
			case "delete_infoblocks":
				echo resp_delete_infoblock($this, $data);
			break;
			case "delete_file":
				echo resp_delete_file($this, $data);
				break;
			// Android APP API ---
			case "app_register_user":
				echo resp_app_user_register($this, $data);
			break;
			case "update_position":
				echo resp_app_update_position($this, $data);
			break;
			case "verify_user":
				echo resp_app_verify_user($this, $data);
			break;
			case "all_app_users":
				echo resp_all_app_users($this, $data);
			break;
			case "radius_search":
				echo resp_app_user_radius_search($this, $data);
			break;
			case "is_username_available":
				echo resp_app_user_available($this, $data);
			break;
			case "test_isUser":
				$this->load->model('app_model');
				echo $this->app_model->usernameavailable("Thusboll") ? "1" : "0";
			break;
			// Jonas APP API ---
			case "jon_user_exists":
				echo jon_app_user_exists($this, $data);
			break;
			case "jon_update_position":
				echo jon_app_update_position($this, $data);
			break;
			case "jon_initiate_user":
				echo jon_app_initiate_user($this, $data);
			break;
			case "jon_get_other_users":
				echo jon_app_get_other_users($this, $data);
			break;
			case "jon_app_association_exists":
				echo jon_app_association_exists($this, $data);
			break;
			case "jon_app_create_association":
				echo jon_app_create_association($this, $data);
			break;
			case "jon_app_get_associations":
				echo jon_app_get_associations($this, $data);
			break;
			case "jon_app_delete_association":
				echo jon_app_delete_association($this, $data);
			break;
			case "jon_app_delete_user":
				echo jon_app_delete_user($this, $data);
			break;
		}
	}
}