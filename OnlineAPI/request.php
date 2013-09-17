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
			case "update_position":
				echo resp_app_update_position($this, $data);
			break;
			case "is_username_available":
				echo resp_app_user_exists($this, $data);
			break;
			case "is_bob_available":
			$this->load->model('app_model');
			echo $this->app_model->usernameavailable("Bob") ? "1" : "0";
			break;
			case "is_ann_available":
			$this->load->model('app_model');
			echo $this->app_model->usernameavailable("Ann") ? "1" : "0";
			break;
		}
	}
}