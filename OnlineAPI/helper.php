<?php
/**********************************/
/********** ANDROID APP ***********/
/**********************************/
if(!function_exists('resp_app_user_exists'))
{
	function resp_app_user_exists($obj, $data)
	{
		$obj->load->model('app_model');
		$user = $obj->input->post('username');
		return $obj->app_model->userexists($user);
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