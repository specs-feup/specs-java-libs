/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.treenode.transform.transformations;

import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.transform.TwoOperandTransform;

public class AddChildTransform<K extends TreeNode<K>> extends TwoOperandTransform<K> {

    private final Integer position;

    public AddChildTransform(K baseNode, K newNode) {
        this(baseNode, newNode, null);
    }

    public AddChildTransform(K baseNode, K newNode, Integer position) {
        super("add-child", baseNode, newNode);

        this.position = position;
    }

    @Override
    public void execute() {
        if (position == null) {
            getBaseNode().addChild(getNewNode());
            return;
        }

        getBaseNode().addChild(position, getNewNode());
    }

}
