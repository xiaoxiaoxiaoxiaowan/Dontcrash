
public abstract class command {
    emodel ed = null;
    abstract public void redo();
    abstract public void undo();
    public command(emodel e){
        ed = e;
    }
}


class addcommand extends command{
    displayable block = null;
    public addcommand(emodel ed, displayable block){
        super(ed);
        this.block = block;
        this.redo();
    }

    @Override
    public void redo(){
        ed.add(block);
    }

    @Override
    public void undo(){
        ed.remove(block);
    }
}


class deletecommand extends command{
    displayable block = null;
    public deletecommand(emodel ed, displayable block){
        super(ed);
        this.block = block;
        this.redo();
    }

    @Override
    public void redo(){
        ed.remove(block);
    }

    @Override
    public void undo(){
        ed.add(block);
    }
}

class movecommand extends command{
    displayable block = null;
    int fromx = 0;
    int fromy = 0;
    int tox = 0;
    int toy = 0;


    public movecommand(emodel ed, displayable block, int fromx, int fromy){
        super(ed);
        this.block = block;
        this.fromx = fromx;
        this.fromy = fromy;
        this.tox = block.x;
        this.toy = block.y;
    }

    @Override
    public void redo(){
        block.x = tox;
        block.y = toy;
    }

    @Override
    public void undo(){
        block.x = fromx;
        block.y = fromy;
    }
}