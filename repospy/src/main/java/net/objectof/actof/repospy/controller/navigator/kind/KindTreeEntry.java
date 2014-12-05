package net.objectof.actof.repospy.controller.navigator.kind;

import net.objectof.actof.common.util.RepoUtils;
import net.objectof.model.Resource;

public class KindTreeEntry {

	public String entityKind;
	public Resource<?> res;
	
	public KindTreeEntry(String entityKind) {
		this.entityKind = entityKind;
	}
	
	public KindTreeEntry(Resource<?> res) {
		this.res = res;
	}
	
	@Override
	public String toString() {
		if (res == null) { return entityKind; }
		return RepoUtils.resToString(res);
	}
	
}
