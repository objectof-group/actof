package net.objectof.actof.common.controller.repository;

import java.util.ArrayList;
import java.util.List;

import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;

public class RepositoryModel {

	public Package repo;
	public Transaction stagingTx;
	public Transaction cleanTx;
	public List<Resource<?>> transients = new ArrayList<>();
	
}
